package com.twtstudio.service.tjwhm.exam.list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R

class ListActivity : AppCompatActivity() {

    lateinit var rvList: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exam_activity_list)

        rvList = findViewById(R.id.rv_list)

        rvList.apply {
            layoutManager = LinearLayoutManager(this.context)
        }

        rvList.withItems {
            repeat(10) {
                lessonItem(this@ListActivity, this@ListActivity)
            }
        }

    }
}
