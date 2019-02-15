package com.twt.service.ecard.view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.Window
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.wepeiyang.commons.ui.rec.withItems

class EcardMainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.ecard_activity_main)
        window.statusBarColor = Color.parseColor("#ffeb86")
        val toolbar: Toolbar = findViewById(R.id.tb_main)
        val titleOfToolbar: TextView = findViewById(R.id.tv_main_title)
        titleOfToolbar.text = "校园卡"
        toolbar.apply {
            title = " "
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        recyclerView = findViewById(R.id.rv_main_main)
        recyclerView.layoutManager = LinearLayoutManager(this@EcardMainActivity)
        val itemManager = recyclerView.withItems(mutableListOf())
    }
}