package com.twt.service.theory.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.twt.service.theory.R
import com.twt.service.theory.model.TheoryApi
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.theory_activity_user.*
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.android.synthetic.main.theory_common_toolbar.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch(UI) {
            val dat = TheoryApi.getSession().await()
            Log.d("BUG", dat.string())
        }
        setContentView(R.layout.theory_activity_user)
        initView()
    }

    private fun initView() {
        window.statusBarColor = Color.parseColor("#FFFFFF")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        enableLightStatusBarMode(true)
        theory_back.setOnClickListener {
            finish()
        }
        theory_user_actionbar_1.title.text = "个人页面"
        theory_search.visibility = View.GONE
        theory_person_profile.visibility = View.GONE
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
