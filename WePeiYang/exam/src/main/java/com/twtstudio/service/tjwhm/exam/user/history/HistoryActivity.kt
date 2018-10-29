package com.twtstudio.service.tjwhm.exam.user.history

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.commons.joinQQGroupForHelp
import com.twtstudio.service.tjwhm.exam.user.OneHistoryBean
import com.twtstudio.service.tjwhm.exam.user.examUserHistoryLiveData

/**
 * Created by tjwhm@TWTStudio at 5:17 PM,2018/8/9.
 * Happy coding!
 */

class HistoryActivity : AppCompatActivity() {

    private var statusBarView: View? = null

    private lateinit var rvHistory: RecyclerView
    private lateinit var ivNoRecord: ImageView
    private lateinit var tvNoRecord: TextView

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
        findViewById<ImageView>(R.id.iv_history_help).setOnClickListener { joinQQGroupForHelp() }
        rvHistory = findViewById(R.id.rv_history)
        ivNoRecord = findViewById(R.id.iv_history_no_record)
        tvNoRecord = findViewById(R.id.tv_history_no_record)
        rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)

        examUserHistoryLiveData.bindNonNull(this, ::bindHistoryData)
    }

    private fun bindHistoryData(list: List<OneHistoryBean>) {
        if (list.isEmpty()) {
            ivNoRecord.visibility = View.VISIBLE
            tvNoRecord.visibility = View.VISIBLE
        } else rvHistory.withItems {
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
