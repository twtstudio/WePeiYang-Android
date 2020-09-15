package com.twt.service.announcement.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twt.service.announcement.R

/**
 * DetailActivity
 * @author TranceDream
 * 点进问题后显示的问题详情页面，显示详细问题和简略评论
 */
class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}