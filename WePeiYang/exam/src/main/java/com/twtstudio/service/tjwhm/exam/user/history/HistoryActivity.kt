package com.twtstudio.service.tjwhm.exam.user.history

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.user.OneHistoryBean
import com.twtstudio.service.tjwhm.exam.user.examUserHistoryLiveData

/**
 * Created by tjwhm@TWTStudio at 5:17 PM,2018/8/9.
 * Happy coding!
 */

class HistoryActivity : AppCompatActivity() {

    private var statusBarView: View? = null

    private lateinit var rvHistory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.exam_activity_history)

        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }

        findViewById<ImageView>(R.id.iv_history_back).setOnClickListener { onBackPressed() }
        rvHistory = findViewById(R.id.rv_history)
        rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)

        examUserHistoryLiveData.bindNonNull(this, ::bindHistoryData)
    }

    private fun bindHistoryData(list: List<OneHistoryBean>) {
        rvHistory.withItems {
            repeat(list.size) {
                historyItem(this@HistoryActivity, list[it])
            }
        }
    }

    private fun initStatusBar() {
        if (statusBarView == null) {
            val identifier = resources.getIdentifier("statusBarBackground", "id", "android")
            statusBarView = window.findViewById(identifier)
        }
        statusBarView?.setBackgroundResource(R.drawable.exam_shape_statusbar_gradient)
    }
}
