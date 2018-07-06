package com.twtstudio.service.dishesreviews.share

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.twtstudio.service.dishesreviews.R
import kotlinx.android.synthetic.main.dishes_reviews_activity_share.*

class ShareActivity : AppCompatActivity() {
    private val qrCodeUtil = QRCodeUtil()
    val shootUtil = ScreenShootUtil()
    val STRFILE = Environment.getExternalStorageDirectory().absolutePath + "/Pictures"
    private var foodId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_share)
        foodId = intent.getIntExtra("FoodId", 0)
        img_qrcode.setImageBitmap(qrCodeUtil.createQRCodeWithLogo("http://www.baidu.com", 108, 108, BitmapFactory.decodeResource(resources, R.drawable.dishes_reviews_qrlogo)))//108具体根据库内renderResult函数原理计算
        imgbt_save.setOnClickListener {
            val res = shootUtil.shoot(this)
            shootUtil.save(res, STRFILE, this)
        }
    }

}