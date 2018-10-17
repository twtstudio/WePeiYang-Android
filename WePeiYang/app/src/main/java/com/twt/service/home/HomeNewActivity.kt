package com.twt.service.home

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.twt.service.AppPreferences
import com.twt.service.R
import com.twt.service.home.other.homeOthers
import com.twt.service.home.user.FragmentActivity
import com.twt.service.schedule2.view.home.homeScheduleItem
import com.twt.service.tjunet.view.homeTjuNetItem
import com.twt.service.widget.ScheduleWidgetProvider
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twt.wepeiyang.commons.view.RecyclerViewDivider
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.retrox.tjulibrary.home.libraryHomeItem
import io.multimoon.colorful.CAppCompatActivity
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity
import pub.devrel.easypermissions.EasyPermissions
import xyz.rickygao.gpa2.view.gpaHomeItem
import xyz.rickygao.gpa2.view.gpaNewHomeItem


class HomeNewActivity : CAppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EasyPermissions.requestPermissions(this, "微北洋需要外部存储来提供必要的缓存\n 需要位置信息来获取校园网连接状态", 0, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

        setContentView(R.layout.activity_home_new)
        enableLightStatusBarMode(true)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.WHITE
        }
        val imageClickIntent = Intent(this@HomeNewActivity, ScheduleWidgetProvider::class.java).apply {
            setAction("com.twt.appwidget.refresh")
        }
        sendBroadcast(imageClickIntent)
        val imageView = findViewById<ImageView>(R.id.iv_toolbar_avatar).apply {
            setOnClickListener {
                startActivity<FragmentActivity>("frag" to "User")
            }
        }
        authSelfLiveData.bindNonNull(this) {
            Glide.with(this).load(it.avatar).into(imageView)
        }

        val rec = findViewById<RecyclerView>(R.id.rec_main)
        rec.apply {
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(RecyclerViewDivider.Builder(this@HomeNewActivity).setSize(4f).setColor(Color.TRANSPARENT).build())
        }
        rec.withItems {
            homeScheduleItem(this@HomeNewActivity)
            if (AppPreferences.isDisplayGpa) {
                gpaNewHomeItem(this@HomeNewActivity)
            }
            libraryHomeItem(this@HomeNewActivity)
            homeTjuNetItem(this@HomeNewActivity)
            homeOthers()
        }
        rec.post {
            (rec.getChildAt(0).layoutParams as RecyclerView.LayoutParams).topMargin = dip(4)
        }
    }
}