package com.twtstudio.service.tjwhm.exam.user.star

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Looper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.refreshAll
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.commons.joinQQGroupForHelp
import com.twtstudio.service.tjwhm.exam.user.getCollections
import es.dmoral.toasty.Toasty

/**
 * Created by tjwhm@TWTStudio at 12:05 PM,2018/8/15.
 * Happy coding!
 */

class StarActivity : AppCompatActivity() {

    companion object {
        const val TYPE_KEY = "type_key"
        const val STAR = 0
        const val WRONG = 1
    }

    private var statusBarView: View? = null

    private lateinit var tvTitle: TextView
    private lateinit var rvStar: RecyclerView
    private lateinit var srlStar: SwipeRefreshLayout
    private lateinit var ivNoRecord: ImageView
    private lateinit var tvNoRecord: TextView

    private var starOrWrong = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.exam_activity_star)
        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }

        findViewById<ImageView>(R.id.iv_star_back).setOnClickListener { onBackPressed() }
        findViewById<ImageView>(R.id.iv_star_help).setOnClickListener { joinQQGroupForHelp() }
        tvTitle = findViewById(R.id.tv_star_toolbar_title)
        rvStar = findViewById(R.id.rv_star)
        ivNoRecord = findViewById(R.id.iv_star_no_record)
        tvNoRecord = findViewById(R.id.tv_star_no_record)
        srlStar = findViewById<SwipeRefreshLayout>(R.id.srl_star).apply {
            setColorSchemeColors(0x449ff1)
            isRefreshing = true
        }

        starOrWrong = intent.getIntExtra(TYPE_KEY, 0)

        when (starOrWrong) {
            STAR -> {
                tvTitle.text = "我的收藏"
                tvNoRecord.text = "这里还没有收藏题目呢，快去刷题吧~"
            }
            WRONG -> {
                tvTitle.text = "我的错题"
                tvNoRecord.text = "这里还没有错题记录呢，快去刷题吧~"
            }
        }

        srlStar.setOnRefreshListener {
            startStarNetwork()
        }

        rvStar.layoutManager = LinearLayoutManager(this@StarActivity)
        startStarNetwork()
    }

    private fun startStarNetwork() {
        srlStar.isRefreshing = true
        getCollections(starOrWrong.toString()) {
            when (it) {
                is RefreshState.Failure -> Toasty.error(this@StarActivity, "网络错误", Toast.LENGTH_SHORT).show()
                is RefreshState.Success -> {
                    if (it.message.data == null || it.message.data?.size == 0) {
                        ivNoRecord.visibility = View.VISIBLE
                        tvNoRecord.visibility = View.VISIBLE
                    }
                    it.message.data?.apply {
                        rvStar.withItems {
                            for (i in 0 until this@apply.size) {
                                starItem(this@StarActivity, this@apply[i], starOrWrong)
                            }
                        }
                    }
                    srlStar.isRefreshing = false
                }
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
