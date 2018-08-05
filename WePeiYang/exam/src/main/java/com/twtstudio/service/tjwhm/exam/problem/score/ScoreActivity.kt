package com.twtstudio.service.tjwhm.exam.problem.score

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.problem.ScoreViewModel

class ScoreActivity : AppCompatActivity() {


    companion object {
        const val SCORE_VIEW_MODEL_KEY = "score_view_model_key"

    }

    lateinit var scoreData: ScoreViewModel
    lateinit var ivBack: ImageView
    lateinit var tvTitle: TextView
    lateinit var tvProblemNum: TextView
    lateinit var tvRadio: TextView
    lateinit var tvTime: TextView
    lateinit var tvWrongNum: TextView
    lateinit var rvScore: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exam_activity_score)

        scoreData = intent.getSerializableExtra(SCORE_VIEW_MODEL_KEY) as ScoreViewModel
        ivBack = findViewById(R.id.iv_score_back)
        tvTitle = findViewById(R.id.tv_score_title)
        tvProblemNum = findViewById(R.id.tv_score_problem_num)
        tvRadio = findViewById(R.id.tv_score_problem_radio)
        tvTime = findViewById(R.id.tv_score_time)
        tvWrongNum = findViewById(R.id.tv_score_wrong_num)
        rvScore = findViewById(R.id.rv_score)

    }
}
