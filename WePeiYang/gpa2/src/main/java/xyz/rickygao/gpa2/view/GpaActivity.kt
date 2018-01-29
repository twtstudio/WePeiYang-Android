package xyz.rickygao.gpa2.view

import android.arch.lifecycle.MutableLiveData
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.widget.*
import es.dmoral.toasty.Toasty
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.api.GpaBean
import xyz.rickygao.gpa2.api.GpaProvider
import xyz.rickygao.gpa2.api.Stat
import xyz.rickygao.gpa2.api.Term
import xyz.rickygao.gpa2.ext.*

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

    private val selectedTermLiveData = MutableLiveData<Int>().apply { value = 0 }
    private lateinit var courseAdapter: CourseAdapter


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
        GpaProvider.gpaLiveData
                .map(GpaBean::stat)
                .map(Stat::total)
                .bind(this) {
                    it?.let {
                        scoreTv.text = it.score.toString()
                        gpaTv.text = it.gpa.toString()
                        creditTv.text = it.credit.toString()
                    }
                }

        // init term selector
        selectedTermTs = findViewById<TextSwitcher>(R.id.ts_selected_term).apply {
            setFactory {
                inflater.inflate(R.layout.gpa2_tv_selected_term, this@apply, false)
            }
        }
        prevBtn = findViewById(R.id.btn_prev)
        nextBtn = findViewById(R.id.btn_next)
        prevBtn.setOnClickListener {
            val cur = selectedTermLiveData.value ?: return@setOnClickListener
            selectedTermLiveData.value = cur - 1
        }
        nextBtn.setOnClickListener {
            val cur = selectedTermLiveData.value ?: return@setOnClickListener
            selectedTermLiveData.value = cur + 1
        }

        // init line chart
        gpaLineCv = findViewById<GpaLineChartView>(R.id.cv_gpa_line).apply {
            onSelectionChangedListener = { selectedTermLiveData.value = it }
        }
        GpaProvider.gpaLiveData
                .map(GpaBean::data)
                .map {
                    it.map(Term::stat).map {
                        GpaLineChartView.DataWithDetail(it.score, """
                            加权：${it.score}
                            绩点：${it.gpa}
                            学分：${it.credit}
                            """.trimIndent())
                    }
                }
                .bind(this) {
                    it?.let {
                        gpaLineCv.dataWithDetail = it
                    }
                }

        // init radar chart
        gpaRadarCv = findViewById<GpaRadarChartView>(R.id.cv_gpa_radar).apply {
            emptyText = RADAR_EMPTY_TEXT
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

        // attempt to refresh chart view while new data coming
        GpaProvider.gpaLiveData
                .bind(this) {
                    selectedTermLiveData.value = selectedTermLiveData.value
                }

        // observe selected term
        selectedTermLiveData.bind(this) {
            it?.let {
                val term = GpaProvider.gpaLiveData.value?.data.orEmpty()

                if (term.isEmpty()) {
                    selectedTermTs.setText(EMPTY_TERM)
                    tbSelectedTermTv.text = EMPTY_TERM
                    return@bind
                }

                var realIndex = it % term.size
                if (realIndex < 0)
                    realIndex += term.size
                val selectedTerm = term[realIndex]
                selectedTermTs.setText(selectedTerm.name)

                tbSelectedTermTv.text = selectedTerm.name

                gpaLineCv.selectedIndex = realIndex

                gpaRadarCv.dataWithLabel = selectedTerm.data
                        .filter { it.score >= 0 }
                        .map { GpaRadarChartView.DataWithLabel(it.score, it.name) }

                courseAdapter.courses = selectedTerm.data.mapTo(ArrayList(selectedTerm.data.size)) {
                    CourseAdapter.Course(it.name, it.type, it.credit, it.score, it.evaluate)
                }
            }
        }

        // load data
        GpaProvider.updateGpaLiveData()

        // bind callback
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

