package com.twtstudio.service.dishesreviews.share

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twtstudio.service.dishesreviews.R
import kotlinx.android.synthetic.main.dishes_reviews_activity_share.*

class ShareActivity : AppCompatActivity() {
    val qrCodeUtil = QRCodeUtil()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_share)
        img_qrcode.setImageBitmap(qrCodeUtil.createBitmap("http://www.baidu.com", 108, 108))//108具体根据库内renderResult函数原理计算
    }
}