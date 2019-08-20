package com.twt.service.theory.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.theory_common_toolbar.*

class AnswerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theory_activity_answer)
        initView()
        loadQuestions()
    }


    private fun initView() {
        window.statusBarColor = Color.parseColor("#FFFFFF")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        enableLightStatusBarMode(true)
        theory_submit.visibility = View.VISIBLE
        theory_person_profile.visibility = View.GONE
        theory_search.visibility = View.GONE
        theory_back.setOnClickListener {
            finish()
        }
    }

    private fun loadQuestions(){
        val recyclerView = findViewById<RecyclerView>(R.id.theory_exam_questions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.withItems {
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
            setSingleAnsQues()
        }
    }

}
