package com.twt.service.announcement.ui.detail

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View


/**
 * 这里设置一下Toolbar的各种乱七八糟的
 * @param toolbar 要设置的[Toolbar]
 * @param activity [toolbar]所在的Activity
 * @param title 标题栏文字
 * @param onClick 这里是返回按钮的点击监听
 */
fun setToolbar(toolbar: Toolbar, activity: AppCompatActivity, title: String, onClick: (View) -> Unit) {
    activity.apply {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener(onClick)
    }
}