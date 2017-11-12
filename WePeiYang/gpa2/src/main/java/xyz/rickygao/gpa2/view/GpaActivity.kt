package xyz.rickygao.gpa2.view

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.api.*

class GpaActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var nestedSv: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gpa2_activity_gpa)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nestedSv = findViewById(R.id.sv_nested)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.statusBarColor = Color.WHITE;
//        }

        GpaProvider.updateGpaLiveData()

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

        val lineChart = findViewById<GpaLineChartView>(R.id.cv_gpa_line)
        GpaProvider.gpaLiveData
                .map(GpaBean::data)
                .map {
                    it.map(Term::stat).map {
                        GpaLineChartView.DataWithDetails(it.score, """
                            加权：${it.score}
                            绩点：${it.gpa}
                            学分：${it.credit}
                            """.trimIndent())
                    }
                }
                .observe(this, Observer {
                    it ?: return@Observer
                    lineChart.dataWithDetails = it
                })


    }
}
