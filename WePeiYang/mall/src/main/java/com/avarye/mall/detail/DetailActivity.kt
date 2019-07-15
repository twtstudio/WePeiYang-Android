package com.avarye.mall.detail

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.avarye.mall.R
import com.avarye.mall.service.MallManager
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_detail)
        window.statusBarColor = Color.parseColor("#FF9A36")

        id = intent.getStringExtra("id")
    }

    fun getDetail() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getDetail(id).awaitAndHandle {

            }?.let {

            }
        }
    }

}
