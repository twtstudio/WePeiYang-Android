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
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView


class ListActivity : AppCompatActivity() {

    private lateinit var rvList: RecyclerView
    private lateinit var etSearch: EditText

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

        rvList = findViewById(R.id.rv_list)
        etSearch = findViewById(R.id.et_search_list)
        etSearch.visibility = View.GONE
        findViewById<ImageView>(R.id.iv_list_back).setOnClickListener { onBackPressed() }
        val type = intent.getStringExtra(LESSON_TYPE)
        if (type == ONLINE) etSearch.visibility = View.VISIBLE
        rvList.apply {
            layoutManager = LinearLayoutManager(this.context)
        }
        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }

        getList(type) {
            when (it) {
                is RefreshState.Failure -> {
                    Toasty.error(this@ListActivity, "网络错误", Toast.LENGTH_SHORT).show()
                }
                is RefreshState.Success -> {

                    rvList.withItems {
                        Log.d("zzzz", "${it.message.date.size}")
                        for (i in 0 until it.message.date.size) {
                            lessonItem(this@ListActivity, it.message.date[i])
                            Log.d("zzzz", "$i")
                            Log.d("zzzz", it.message.date[i].course_name)

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
