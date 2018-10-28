package com.twtstudio.service.tjwhm.exam.problem.score

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.problem.ScoreBean
import com.twtstudio.service.tjwhm.exam.problem.TestBean

class ScoreActivity : AppCompatActivity() {

    companion object {
        const val SCORE_BEAN_KEY = "score_view_model_key"
        const val PROBLEM_FOR_TEST_KEY = "problem_for_test_key"
        const val TEST_TIME_KEY = "test-time-key"
    }

    private lateinit var scoreBean: ScoreBean
    private lateinit var testBean: TestBean

    private lateinit var toolbar: Toolbar

    private lateinit var rvScore: RecyclerView

    private var statusBarView: View? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exam_activity_score)
        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }

        scoreBean = intent.getSerializableExtra(SCORE_BEAN_KEY) as ScoreBean
        testBean = intent.getSerializableExtra(PROBLEM_FOR_TEST_KEY) as TestBean

        val testTime = intent.getLongExtra(TEST_TIME_KEY, 0L)

        toolbar = findViewById(R.id.tb_score)
        findViewById<ImageView>(R.id.iv_score_back).setOnClickListener { onBackPressed() }

        rvScore = findViewById(R.id.rv_score)

        rvScore.layoutManager = LinearLayoutManager(this@ScoreActivity)
        rvScore.withItems {
            scoreHeaderItem(testTime, scoreBean)
            repeat(scoreBean.result.size) {
                scoreItem(it, this@ScoreActivity, testBean.question[it], scoreBean.result[it])
            }
        }
    }

    private fun initStatusBar() {
        if (statusBarView == null) {
            val identifier = resources.getIdentifier("statusBarBackground", "id", "android")
            statusBarView = window.findViewById(identifier)
        }
        statusBarView?.setBackgroundResource(R.drawable.exam_shape_statusbar_gradient)
    }
}
