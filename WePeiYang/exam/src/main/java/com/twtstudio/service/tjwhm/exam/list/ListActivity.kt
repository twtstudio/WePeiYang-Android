package com.twtstudio.service.tjwhm.exam.list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.githang.statusbar.StatusBarCompat
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty
import android.os.Looper
import android.os.MessageQueue
import android.view.View
import android.widget.ImageView


class ListActivity : AppCompatActivity() {

    private lateinit var rvList: RecyclerView

    companion object {
        const val LESSON_TYPE = "lesson_type"
        const val PARTY = "2"
        const val POLICY = "1"
        const val ONLINE = "3"
        const val MORE = "more"
    }

    private var statusBarView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exam_activity_list)
        StatusBarCompat.setStatusBarColor(this@ListActivity, ContextCompat.getColor(this@ListActivity, R.color.examToolbarBlue), true)
        rvList = findViewById(R.id.rv_list)
        findViewById<ImageView>(R.id.iv_list_back).setOnClickListener { onBackPressed() }
        rvList.apply {
            layoutManager = LinearLayoutManager(this.context)
        }
        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }

        getList(intent.getStringExtra(LESSON_TYPE)) {
            when (it) {
                is RefreshState.Failure -> {
                    Toasty.error(this@ListActivity, "网络错误", Toast.LENGTH_SHORT).show()
                }
                is RefreshState.Success -> {

                    rvList.withItems {
                        for (i in 0 until it.message.date.size) {
                            lessonItem(this@ListActivity, it.message.date[i])
                        }
                    }
                }
            }
        }
    }


    private fun initStatusBar() {
        if (statusBarView == null) {
            val identifier = resources.getIdentifier("statusBarBackground", "id", "android")
            statusBarView = window.findViewById(identifier)
        }
        statusBarView?.setBackgroundResource(R.drawable.exam_statusbar_gradient)
    }

}
