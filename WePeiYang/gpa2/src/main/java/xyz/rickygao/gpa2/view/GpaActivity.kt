package xyz.rickygao.gpa2.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextSwitcher
import android.widget.TextView
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.api.GpaBean
import xyz.rickygao.gpa2.api.GpaProvider
import xyz.rickygao.gpa2.api.Stat
import xyz.rickygao.gpa2.api.Term
import xyz.rickygao.gpa2.ext.map
import xyz.rickygao.gpa2.ext.setLightStatusBarMode

/**
 * Created by rickygao on 2017/11/9.
 */
class GpaActivity : AppCompatActivity() {

    private lateinit var inflater: LayoutInflater
    private lateinit var toolbar: Toolbar
    private lateinit var backBtn: ImageButton
    private lateinit var tbSelectedTermTv: TextView
    private lateinit var nestedSv: NestedScrollView
    private val selectedTermLiveData = MutableLiveData<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gpa2_activity_gpa)

        setLightStatusBarMode(true)

        inflater = LayoutInflater.from(this)

        // init toolbar
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tbSelectedTermTv = findViewById<TextView>(R.id.tv_toolbar_selected_term)
        backBtn = findViewById<ImageButton>(R.id.btn_back).apply {
            setOnClickListener {
                onBackPressed()
            }
        }
        backBtn = findViewById<ImageButton>(R.id.btn_refresh).apply {
            setOnClickListener {
                GpaProvider.updateGpaLiveData(false)
            }
        }

        nestedSv = findViewById(R.id.sv_nested)

        // init total
        val scoreTv = findViewById<TextView>(R.id.tv_score)
        val gpaTv = findViewById<TextView>(R.id.tv_gpa)
        val creditTv = findViewById<TextView>(R.id.tv_credit)
        GpaProvider.gpaLiveData
                .map(GpaBean::stat)
                .map(Stat::total)
                .observe(this, Observer {
                    it?.let {
                        scoreTv.text = it.score.toString()
                        gpaTv.text = it.gpa.toString()
                        creditTv.text = it.credit.toString()
                        Snackbar.make(nestedSv, "加载成功", Snackbar.LENGTH_LONG).show()
                    }
                })

        // init term selector
        val selectedTermTs = findViewById<TextSwitcher>(R.id.ts_selected_term).apply {
            setFactory {
                inflater.inflate(R.layout.gpa2_tv_selected_term, this@apply, false)
            }
        }
        val prevBtn = findViewById<ImageButton>(R.id.btn_prev)
        val nextBtn = findViewById<ImageButton>(R.id.btn_next)
        prevBtn.setOnClickListener {
            val cur = selectedTermLiveData.value ?: return@setOnClickListener
            selectedTermLiveData.value = cur - 1
        }
        nextBtn.setOnClickListener {
            val cur = selectedTermLiveData.value ?: return@setOnClickListener
            selectedTermLiveData.value = cur + 1
        }

        // init line chart
        val gpaLineCv = findViewById<GpaLineChartView>(R.id.cv_gpa_line).apply {
            onSelectDataListenner = { selectedTermLiveData.value = it }
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
                .observe(this, Observer {
                    it ?: return@Observer
                    gpaLineCv.dataWithDetail = it
                })

        // init radar chart
        val gpaRadarCv = findViewById<GpaRadarChartView>(R.id.cv_gpa_radar).apply {
            emptyText = "出了分再来吧！"
        }

        // init course list
        val courseAdapter = CourseAdapter(inflater).apply {
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
                    else -> CourseAdapter.SORT_DEFAULT
                }
            }
        }

        // attempt to refresh chart view while new data coming
        GpaProvider.gpaLiveData
                .observe(this, Observer {
                    selectedTermLiveData.value = selectedTermLiveData.value ?: 0
                })

        // observe selected term
        selectedTermLiveData.observe(this, Observer {
            it?.let {
                val term = GpaProvider.gpaLiveData.value?.data.orEmpty()

                if (term.isEmpty()) return@Observer

                var realIndex = it % term.size
                if (realIndex < 0)
                    realIndex += term.size
                val selectedTerm = term[realIndex]
                selectedTermTs.setText(selectedTerm.name)

                tbSelectedTermTv.text = selectedTerm.name

                gpaLineCv.selectedIndex = realIndex

                gpaRadarCv.dataWithLabel = selectedTerm.data.map {
                    GpaRadarChartView.DataWithLabel(it.score, it.name)
                }

                courseAdapter.courses = selectedTerm.data.mapTo(ArrayList(selectedTerm.data.size)) {
                    CourseAdapter.Course(it.name, it.type, it.credit, it.score)
                }
            }
        })

        // radar chart rotates while scrolling
        nestedSv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener({ view, x, y, oldx, oldy ->
            gpaRadarCv.startRad = y.toDouble() / view.height * 2 * Math.PI
        }))

        // load data
        GpaProvider.updateGpaLiveData()
    }
}
