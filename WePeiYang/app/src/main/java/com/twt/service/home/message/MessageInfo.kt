package com.twt.service.home.message

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.twt.service.R
import kotlinx.android.synthetic.main.activity_message_info.*


class MessageInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_info)
        window.statusBarColor = Color.parseColor("#646887")
//        val detor = window.decorView
//        var ui = detor.systemUiVisibility
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ui = ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }
//        detor.systemUiVisibility=ui
        message_info_back_arrow.setOnClickListener{ onBackPressed() }
        message_info_title.text = intent.getStringExtra("title")
        message_info_time.text = intent.getStringExtra("time")
        message_info_content.text = intent.getStringExtra("content")
    }
}
