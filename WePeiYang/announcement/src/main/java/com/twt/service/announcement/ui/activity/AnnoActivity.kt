package com.twt.service.announcement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.twt.service.announcement.R
import com.twt.service.announcement.detail.DetailActivity
import com.twt.service.announcement.service.AnnoService
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick

class AnnoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anno)


        GlobalScope.launch {
            val string = AnnoService.Companion.get().awaitAndHandle{it.printStackTrace()}?.data.toString()
            Log.d("tag_tree",string)
        }

        findViewById<FloatingActionButton>(R.id.fl_btn).apply {
            this.onClick {
                startActivity(Intent(this@AnnoActivity,DetailActivity::class.java))
            }
        }


    }
}