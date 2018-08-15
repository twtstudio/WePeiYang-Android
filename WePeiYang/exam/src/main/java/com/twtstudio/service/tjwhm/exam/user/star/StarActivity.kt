package com.twtstudio.service.tjwhm.exam.user.star

import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.user.getCollections
import es.dmoral.toasty.Toasty

/**
 * Created by tjwhm@TWTStudio at 12:05 PM,2018/8/15.
 * Happy coding!
 */

class StarActivity : AppCompatActivity() {

    private var statusBarView: View? = null

    private lateinit var tvTitle: TextView
    private lateinit var rvStar: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exam_activity_star)
        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }

        findViewById<ImageView>(R.id.iv_star_back).setOnClickListener { onBackPressed() }
        tvTitle = findViewById(R.id.tv_star_toolbar_title)
        rvStar = findViewById(R.id.rv_star)

        tvTitle.text = "我的收藏"

        rvStar.layoutManager = LinearLayoutManager(this@StarActivity)
        getCollections("0") {
            when (it) {
                is RefreshState.Failure -> Toasty.error(this@StarActivity, "网络错误", Toast.LENGTH_SHORT).show()
                is RefreshState.Success -> {
                    rvStar.withItems {
                        for (i in 0 until it.message.ques.size) {
                            starItem(this@StarActivity, it.message.ques[i].ques)
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