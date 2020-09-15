package com.twt.service.announcement.detail

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import cn.edu.twt.retrox.recyclerviewdsl.withItems
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R

/**
 * DetailActivity
 * @author TranceDream
 * 点进问题后显示的问题详情页面，显示详细问题和简略评论
 * TODO: 进入该Activity应该将问题数据传入
 */
class DetailActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView

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
    }

    /**
     * findViewById罢了
     */
    private fun findViews() {
        toolbar = findViewById(R.id.annoCommonToolbar)
        recyclerView = findViewById(R.id.annoDetailRecyclerView)
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
                        "如何评价天津大如何评价天津大学校如何评务管理系统学校务管理系统",
                        "如何评价天津大学校务管理系统如何评价天津大学校务管理系统如何评价天津大学校务管理系统如何评价天津大学校务管理系统",
                        "蒙古上单",
                        1,
                        "1895年一个阳光明媚的夜晚"
                )
            }
        }
    }
}