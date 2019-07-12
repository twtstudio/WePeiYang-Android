package com.avarye.mall.detail

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.avarye.mall.R
import kotlinx.android.synthetic.main.mall_activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_detail)
        window.statusBarColor = Color.parseColor("#FF9A36")

        tv_detail_name.text = intent.getStringExtra("name")
        tv_detail_seller.text = intent.getStringExtra("seller")
        //像这样拿过来,或者看怎么好看吧
    }
}
