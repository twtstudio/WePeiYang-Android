package com.twt.service.announcement.ui.detail

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import cn.edu.twt.retrox.recyclerviewdsl.withItems
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.Reply
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.Dispatchers
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

    private lateinit var title: String
    private lateinit var reply: Reply
    private var replyIndex: Int = 0
    private var questionId: Int = 0
    private var userId: Int = 0
    private var likeCount: Int = 0
    private var likeState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)
        // 设置状态栏的颜色
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        findViews()
        getDataFromPreviousActivity()
        setToolbar(toolbar, this, "回复详情") {
            finish()
        }
        setRefresh()
        GlobalScope.launch {
            getData()
        }.invokeOnCompletion {
            runOnUiThread {
                setRecyclerView()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    /**
     * 获取传入的数据
     */
    private fun getDataFromPreviousActivity() {
        title = intent.getStringExtra("title")
        reply = intent.getSerializableExtra("reply") as Reply
        questionId = intent.getIntExtra("questionId", 0)
        userId = intent.getIntExtra("userId", 0)
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
                        title,
                        reply,
                        likeState,
                        likeCount
                ) {
                    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                        getData()
                    }.invokeOnCompletion {
                        runOnUiThread {
                            swipeRefreshLayout.isRefreshing = false
                            setRecyclerView()
                        }
                    }
                }
                if (userId == AnnoPreference.myId) {
//                if (AnnoPreference.myId == 18) {
                    addReplyRatingItem(userId, reply.id, reply.score)
                }
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
     * // TODO: 这个要做，现在处于鸽鸽状态
     */
    private suspend fun getData() {
        swipeRefreshLayout.isRefreshing = true
        AnnoService.getAnswer(questionId, AnnoPreference.myId!!).awaitAndHandle {
            Toasty.error(this, "拉取数据失败，请稍后再试").show()
        }?.data?.let {
            reply = it[replyIndex]
            likeCount = it[replyIndex].likes
            AnnoService.getLikedState("answer", AnnoPreference.myId!!, reply.id).awaitAndHandle {
                Toasty.error(this, "拉取数据失败，请稍后再试").show()
            }?.data?.let {
                likeState = it.is_liked
            }
        }
    }
}