package xyz.rickygao.gpa2.view

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.twt.wepeiyang.commons.experimental.*
import es.dmoral.toasty.Toasty
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.api.GpaProvider
import xyz.rickygao.gpa2.api.Term

/**
 * Created by rickygao on 2017/11/9.
 */
class GpaActivity : AppCompatActivity() {

    companion object {
        const val EMPTY_TERM = "还没有学期"
        const val RADAR_EMPTY_TEXT = "还没有数据"
    }

    private lateinit var inflater: LayoutInflater
    private lateinit var toolbar: Toolbar
    private lateinit var backBtn: ImageButton
    private lateinit var tbSelectedTermTv: TextView
    private lateinit var refreshBtn: ImageButton

    private lateinit var nestedSv: NestedScrollView
    private lateinit var containerLl: LinearLayout

    private lateinit var scoreTv: TextView
    private lateinit var gpaTv: TextView
    private lateinit var creditTv: TextView

    private lateinit var prevBtn: ImageButton
    private lateinit var selectedTermTs: TextSwitcher
    private lateinit var nextBtn: ImageButton

    private lateinit var gpaLineCv: GpaLineChartView
    private lateinit var gpaRadarCv: GpaRadarChartView

    private lateinit var courseAdapter: CourseAdapter
    private var selectedTermIndex = 0
        set(value) {
            val term = GpaProvider.gpaLiveData.value?.data.orEmpty()
            if (term.isEmpty()) {
                selectedTermTs.setText(EMPTY_TERM)
                tbSelectedTermTv.text = EMPTY_TERM
                courseAdapter.courses = mutableListOf()
                return
            }

            val realIndex = value.coerceIn(if (term.isNotEmpty()) term.indices else 0..0)
            val selectedTerm = term[realIndex]

            prevBtn.visibility = if (realIndex > 0) View.VISIBLE else View.INVISIBLE
            nextBtn.visibility = if (realIndex < term.lastIndex) View.VISIBLE else View.INVISIBLE
            selectedTermTs.setText(selectedTerm.name)
            tbSelectedTermTv.text = selectedTerm.name
            gpaLineCv.selectedIndex = realIndex
            gpaRadarCv.dataWithLabel = selectedTerm.data.asSequence()
                    .filter { it.score >= 0 && it.evaluate == null }
                    .map { GpaRadarChartView.DataWithLabel(it.score, it.name) }
                    .toList()
            courseAdapter.courses = selectedTerm.data.asSequence()
                    .map { CourseAdapter.Course(it.name, it.type, it.credit, it.score, it.evaluate) }
                    .toMutableList()

            field = realIndex
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gpa2_activity_gpa)

        inflater = LayoutInflater.from(this)

        enableLightStatusBarMode(true)

        // init toolbar
        toolbar = findViewById<Toolbar>(R.id.toolbar).also {
            fitSystemWindowWithStatusBar(it)
            setSupportActionBar(it)
        }
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
        tbSelectedTermTv = findViewById(R.id.tv_toolbar_selected_term)
        backBtn = findViewById<ImageButton>(R.id.btn_back).apply {
            setOnClickListener {
                onBackPressed()
            }
        }
        refreshBtn = findViewById<ImageButton>(R.id.btn_refresh).apply {
            setOnClickListener {
                GpaProvider.updateGpaLiveData(false)
            }
        }

        // init container layout
        nestedSv = findViewById<NestedScrollView>(R.id.sv_nested).apply {
            setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { view, _, y, _, _ ->
                // radar chart rotates while scrolling
                gpaRadarCv.startRad = y.toDouble() / view.height * 2 * Math.PI

                // hide title text automatically
                val visibleRect = Rect()
                val visibleRatio = if (selectedTermTs.getGlobalVisibleRect(visibleRect))
                    (visibleRect.height().toFloat() / selectedTermTs.height) else 0F
                tbSelectedTermTv.alpha = 1F - visibleRatio
                selectedTermTs.alpha = visibleRatio
            })
        }
        containerLl = findViewById<LinearLayout>(R.id.ll_container)
                .also(this::fitSystemWindowWithNavigationBar)

        // init total
        scoreTv = findViewById(R.id.tv_score)
        gpaTv = findViewById(R.id.tv_gpa)
        creditTv = findViewById(R.id.tv_credit)

        // init term selector
        selectedTermTs = findViewById<TextSwitcher>(R.id.ts_selected_term).apply {
            setFactory {
                inflater.inflate(R.layout.gpa2_tv_selected_term, this@apply, false)
            }
        }
        prevBtn = findViewById(R.id.btn_prev)
        nextBtn = findViewById(R.id.btn_next)
        prevBtn.setOnClickListener { --selectedTermIndex }
        nextBtn.setOnClickListener { ++selectedTermIndex }

        // init line chart
        gpaLineCv = findViewById<GpaLineChartView>(R.id.cv_gpa_line).apply {
            onSelectionChangedListener = { selectedTermIndex = it }
        }

        // init radar chart
        gpaRadarCv = findViewById<GpaRadarChartView>(R.id.cv_gpa_radar).apply {
            emptyText = RADAR_EMPTY_TEXT
            setOnLongClickListener {
                (it as GpaRadarChartView).dataWithLabel = it.dataWithLabel.shuffled()
                return@setOnLongClickListener true
            }
        }

        // init course list
        courseAdapter = CourseAdapter(inflater).apply {
            sortMode = CourseAdapter.SORT_BY_SCORE_DESC
        }
        findViewById<RecyclerView>(R.id.rv_course).apply {
            adapter = courseAdapter
            layoutManager = LinearLayoutManager(this@GpaActivity)
            isNestedScrollingEnabled = false
        }

        // init sort button
        findViewById<RadioGroup>(R.id.rg_sort).apply {
            setOnCheckedChangeListener { _, buttonId ->
                courseAdapter.sortMode = when (buttonId) {
                    R.id.rb_sort_by_credit -> CourseAdapter.SORT_BY_CREDIT_DESC
                    R.id.rb_sort_by_score -> CourseAdapter.SORT_BY_SCORE_DESC
                    else -> CourseAdapter.SORT_BY_DEFAULT
                }
            }
        }

        // load data
        GpaProvider.updateGpaLiveData()

        // bind callback
        GpaProvider.gpaLiveData.bind(this) {
            it?.stat?.total?.let {
                scoreTv.text = it.score.toString()
                gpaTv.text = it.gpa.toString()
                creditTv.text = it.credit.toString()
            }

            it?.data.orEmpty().asSequence().map(Term::stat).map {
                GpaLineChartView.DataWithDetail(it.score, """
                        加权：${it.score}
                        绩点：${it.gpa}
                        学分：${it.credit}
                        """.trimIndent())
            }.toList().let {
                gpaLineCv.dataWithDetail = it
            }

            // attempt to refresh chart view while new data coming
            selectedTermIndex = selectedTermIndex
        }

        GpaProvider.successLiveData.consume(this) {
            it?.let {
                Toasty.success(this, it).show()
            }
        }

        GpaProvider.errorLiveData.consume(this) {
            it?.let {
                Toasty.error(this, it).show()
            }
        }

    }

}

