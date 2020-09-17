package com.twt.service.announcement.ui.activity.detail

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
import jp.wasabeef.recyclerview.animators.LandingAnimator
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        // 设置状态栏的颜色
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        findViews()
        setToolbar("问题详情") {
            finish()
        }
        setRecyclerView()
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
            itemAnimator = LandingAnimator()
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
                addDetailReplyItem(
                        "天津大学 猫先生",
                        "我觉得这是好的，并且没有任何坏处。喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵",
                        "1895年(大概",
                        false,
                        99
                )
                addDetailCommentItem(
                        "小型pusheen",
                        "* 愤怒 *\n喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵喵",
                        "也是1895年(大概",
                        false,
                        2333
                )
            }
        }
    }

    /**
     * 下拉刷新(假的)
     */
    private fun setRefresh() {
        swipeRefreshLayout.onRefresh {
            GlobalScope.launch {
                delay(3000)
                getData()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    /**
     * 获取评论和回复还有点赞状态
     * 还有用户名
     */
    private fun getData() {

    }
}