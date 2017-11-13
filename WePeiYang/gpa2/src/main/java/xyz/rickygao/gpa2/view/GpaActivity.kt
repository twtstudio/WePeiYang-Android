package xyz.rickygao.gpa2.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.TextSwitcher
import android.widget.TextView
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.api.*
import xyz.rickygao.gpa2.ext.setLightStatusBarMode


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
                GpaProvider.updateGpaLiveData()
            }
        }

        nestedSv = findViewById(R.id.sv_nested)

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

        val selectedTermTs = findViewById<TextSwitcher>(R.id.ts_selected_term).apply {
            setFactory {
                inflater.inflate(R.layout.gpa2_layout_selected_term, this@apply, false)
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

        val lineChart = findViewById<GpaLineChartView>(R.id.cv_gpa_line).apply {
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
                    lineChart.dataWithDetail = it
                })

        GpaProvider.gpaLiveData
                .observe(this, Observer {
                    // attempt to refresh chart view
                    selectedTermLiveData.value = selectedTermLiveData.value ?: 0
                })

        val radarChart = findViewById<GpaRadarChartView>(R.id.cv_gpa_radar)

        selectedTermLiveData.observe(this, Observer {
            it?.let {
                val term = GpaProvider.gpaLiveData.value?.data ?: return@Observer
                var realIndex = it % term.size
                if (realIndex < 0)
                    realIndex += term.size
                selectedTermTs.setText(term[realIndex].name)

                tbSelectedTermTv.text = term[realIndex].name

                lineChart.selectedIndex = realIndex

                radarChart.dataWithLabel = term[realIndex].data.map {
                    GpaRadarChartView.DataWithLabel(it.score, it.name)
                }
            }
        })

        nestedSv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener({ view, x, y, oldx, oldy ->
            radarChart.startRad = y.toDouble() / view.width * 2 * Math.PI
        }))

        GpaProvider.updateGpaLiveData()
    }
}
