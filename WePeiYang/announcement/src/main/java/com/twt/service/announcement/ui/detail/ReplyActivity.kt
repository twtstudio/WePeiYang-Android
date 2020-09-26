package com.twt.service.announcement.ui.detail

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import cn.edu.twt.retrox.recyclerviewdsl.withItems
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.ReplyOrCommit
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.onRefresh

/**
 * ReplyActivity
 * @author TranceDream
 * 这个不是发表回复页面
 * 这个是点击回复进入的回复详情
 */
class ReplyActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var likeCount: Int = 0
    private var likeState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)
        findViews()
        setToolbar(toolbar, this, "回复详情") {
            finish()
        }
        setRefresh()
        GlobalScope.launch {
            getData()
        }.invokeOnCompletion {
            runOnUiThread {
                setRecyclerView()
            }
        }
    }

    /**
     * findViewById罢了
     */
    private fun findViews() {
        toolbar = findViewById(R.id.annoCommonToolbar)
        recyclerView = findViewById(R.id.annoReplyRecyclerView)
        swipeRefreshLayout = findViewById(R.id.annoReplySwipeRefreshLayout)
    }

    /**
     * 设置[recyclerView]
     */
    private fun setRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReplyActivity)
            withItems {
                addReplyItem(
                        intent.getStringExtra("title"),
                        intent.getSerializableExtra("reply") as ReplyOrCommit,
                        likeState, likeCount
                )
            }
            adapter = ScaleInAnimationAdapter(adapter)
        }
    }

    /**
     * 设置[swipeRefreshLayout]
     */
    private fun setRefresh() {
        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.custom_peppa_pink)
            onRefresh {
                GlobalScope.launch {
                    getData()
                    delay(3000) // 模拟网络请求的艰辛
                }.invokeOnCompletion {
                    runOnUiThread {
                        setRecyclerView()
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }

    /**
     * 刷新数据
     * // TODO: 假的假的假的
     */
    private suspend fun getData() {
//        delay(1000) // 模拟网络请求的艰辛
        AnnoService.getLikedState("answer", intent.getIntExtra("userId", 0), intent.getIntExtra("id", 0)).awaitAndHandle {

        }
        likeCount += 50
    }
}