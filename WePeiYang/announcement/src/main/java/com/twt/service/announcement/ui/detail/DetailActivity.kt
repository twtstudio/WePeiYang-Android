package com.twt.service.announcement.ui.detail

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import cn.edu.twt.retrox.recyclerviewdsl.withItems
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R
import com.twt.service.announcement.service.ReplyOrCommit
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.onRefresh

/**
 * DetailActivity
 * @author TranceDream
 * 点进问题后显示的问题详情页面，显示详细问题和简略评论
 * TODO: 进入该Activity应该将问题数据传入
 */
class DetailActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val replyList: MutableList<ReplyOrCommit> = mutableListOf()
    private val commentList: MutableList<ReplyOrCommit> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        // 设置状态栏的颜色
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        findViews()
        setToolbar("问题详情") {
            finish()
        }
        GlobalScope.launch {
            getData()
        }.invokeOnCompletion {
            runOnUiThread {
                setRecyclerView()
            }
        }
        setRefresh()
    }

    /**
     * findViewById罢了
     */
    private fun findViews() {
        toolbar = findViewById(R.id.annoCommonToolbar)
        recyclerView = findViewById(R.id.annoDetailRecyclerView)
        swipeRefreshLayout = findViewById(R.id.annoDetailSwipeRefreshLayout)
    }

    /**
     * 这里设置一下Toolbar的各种乱七八糟的
     * @param title 标题栏文字
     * @param onClick 这里是返回按钮的点击监听
     */
    fun setToolbar(title: String, onClick: (View) -> Unit) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener(onClick)
    }

    /**
     * 这里设置一下问题详情页面RecyclerView的相关逻辑
     */
    private fun setRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            withItems {
                addDetailQuestionItem(
                        this@DetailActivity.intent.getStringExtra("title"),
                        this@DetailActivity.intent.getStringExtra("content"),
                        "蒙古上单",
                        this@DetailActivity.intent.getIntExtra("status", 0),
                        this@DetailActivity.intent.getStringExtra("time"),
                        this@DetailActivity.intent.getBooleanExtra("likeState", false),
                        this@DetailActivity.intent.getIntExtra("likeCount", 0)
                )
                replyList.forEach {
                    addDetailReplyItem(it.user_name, it.contain, it.created_at, false, it.likes)
                }
                commentList.forEach {
                    addDetailCommentItem(it.user_name, it.contain, it.created_at, false, it.likes)
                }
            }
            adapter = ScaleInAnimationAdapter(adapter)
        }
    }

    /**
     * 下拉刷新(假的)
     */
    private fun setRefresh() {
        swipeRefreshLayout.onRefresh {
            GlobalScope.launch {
                getData()
                delay(3000)

                runOnUiThread {
                    setRecyclerView()
                }
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    /**
     * 获取评论和回复还有点赞状态
     * 还有用户名
     */
    private suspend fun getData() {
        // TODO: 这里是假请求
        delay(1000)
        replyList.add(ReplyOrCommit(114, 514, "校长1", "我觉得还行", -1, "?", 1919, "1895年2月31日", "", listOf("810")))
        replyList.add(ReplyOrCommit(114, 514, "校长2", "我觉得还行", -1, "?", 1919, "1895年2月31日", "", listOf("810")))
        replyList.add(ReplyOrCommit(114, 514, "校长3", "我觉得还行", -1, "?", 1919, "1895年2月31日", "", listOf("810")))
        commentList.add(ReplyOrCommit(114, 514, "天津大学 猫先生", "喵", -1, "?", 1919, "1895年2月31日", "", listOf("810")))
        commentList.add(ReplyOrCommit(114, 514, "天津大学 猫先生", "喵", -1, "?", 1919, "1895年2月31日", "", listOf("810")))
        commentList.add(ReplyOrCommit(114, 514, "天津大学 猫先生", "喵", -1, "?", 1919, "1895年2月31日", "", listOf("810")))

    }
}