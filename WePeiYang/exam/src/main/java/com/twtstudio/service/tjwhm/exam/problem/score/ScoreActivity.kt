package com.twtstudio.service.tjwhm.exam.problem.score

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.problem.ScoreViewModel
import com.twtstudio.service.tjwhm.exam.problem.TestViewModel

class ScoreActivity : AppCompatActivity() {


    companion object {
        const val SCORE_VIEW_MODEL_KEY = "score_view_model_key"
        const val PROBLEM_FOR_TEST_KEY = "problem_for_test_key"
    }

    private lateinit var problemData: TestViewModel
    private lateinit var scoreData: ScoreViewModel

    private lateinit var toolbar: Toolbar

    private lateinit var llScore: LinearLayout
    private lateinit var tvProblemNum: TextView
    private lateinit var tvRadio: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvWrongNum: TextView
    private lateinit var rvScore: RecyclerView

    private var statusBarView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exam_activity_score)
        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }

        problemData = intent.getSerializableExtra(PROBLEM_FOR_TEST_KEY) as TestViewModel
        scoreData = intent.getSerializableExtra(SCORE_VIEW_MODEL_KEY) as ScoreViewModel

        toolbar = findViewById(R.id.tb_score)
        findViewById<ImageView>(R.id.iv_score_back).setOnClickListener { onBackPressed() }

        llScore = findViewById(R.id.ll_score)
        tvProblemNum = findViewById(R.id.tv_score_problem_num)
        tvRadio = findViewById(R.id.tv_score_problem_radio)
        tvTime = findViewById(R.id.tv_score_time)
        tvWrongNum = findViewById(R.id.tv_score_wrong_num)
        rvScore = findViewById(R.id.rv_score)

        tvProblemNum.text = scoreData.result.size.toString()
        var wrongNum = 0
        repeat(scoreData.result.size) {
            if (scoreData.result[it].is_true == 0) wrongNum++
        }
        tvWrongNum.text = wrongNum.toString()
        tvRadio.text = "正确率：${((wrongNum.toDouble() / scoreData.result.size.toDouble()).toString() + "0000").substring(2, 4)}%"

        rvScore.layoutManager = LinearLayoutManager(this@ScoreActivity)
        rvScore.withItems {
            repeat(problemData.data.size) {
                scoreItem(it, this@ScoreActivity, problemData.data[it], scoreData.result[it])
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
