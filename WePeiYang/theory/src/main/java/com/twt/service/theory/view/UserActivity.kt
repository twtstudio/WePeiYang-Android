package com.twt.service.theory.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.theory_activity_user.*
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.android.synthetic.main.theory_common_toolbar.view.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theory_activity_user)
        window.statusBarColor = Color.parseColor("#FFFFFF")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        theory_back.setOnClickListener {
            finish()
        }
        theory_user_actionbar.title.text = "个人页面"
        initInfo()
    }

    private fun initInfo() {
        val recyclerView = findViewById<RecyclerView>(R.id.theory_user_rc)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.withItems {
            setProfileItem()
            setUserExamItem()
            setUserExamItem()
        }
    }
}
