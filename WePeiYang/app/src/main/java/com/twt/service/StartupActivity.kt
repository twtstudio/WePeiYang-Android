package com.twt.service

import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twt.service.home.HomeNewActivity
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twtstudio.retrox.auth.view.LoginActivity
import org.jetbrains.anko.shortcutManager
import org.jetbrains.anko.startActivity
import android.util.Log
import com.tencent.stat.MtaSDkException
import com.tencent.stat.StatService
import com.tencent.stat.e


/**
 * Created by retrox on 2017/1/20.
 */

class StartupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 开启腾讯 MTA 线程
        val mtaAppKey = "AP6CS8KKB77P"
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(this, mtaAppKey,
                    com.tencent.stat.common.StatConstants.VERSION);
            Log.d("MTA", "MTA 初始化成功")
        } catch (e: MtaSDkException) {
            // MTA初始化失败
            Log.d("MTA", "MTA 初始化失败 $e")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) createDynamicShortcuts()

        if (CommonPreferences.isLogin) {
            startActivity<HomeNewActivity>()
        } else startActivity<LoginActivity>()

        finish()
    }


    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun createDynamicShortcuts() {

        val dynamicShortcut1 = ShortcutInfo.Builder(this, "website")
                .setShortLabel("天外天")
                .setLongLabel("天外天官网")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_twt_cloud))
                .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twt.edu.cn")))
                .build()

        val dynamicShortcut2 = ShortcutInfo.Builder(this, "gpa")
                .setShortLabel("GPA")
                .setLongLabel("GPA")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_main_gpa))
                .setIntents(
                        // this dynamic shortcut set up a back stack using Intents, when pressing back, will go to MainActivity
                        // the last Intent is what the shortcut really opened
                        arrayOf(
                                Intent("xyz.rickygao.gpa2.DYNAMIC_OPEN"))// intent's action must be set
                )
                .build()

        val dynamicShortcut3 = ShortcutInfo.Builder(this, "schedule")
                .setShortLabel("课程表")
                .setLongLabel("课程表")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_main_schedule))
                //com.twt.schedule.action.DYNAMIC_OPEN
                .setIntents(
                        // this dynamic shortcut set up a back stack using Intents, when pressing back, will go to MainActivity
                        // the last Intent is what the shortcut really opened
                        arrayOf(
                                Intent("com.twt.schedule.action.DYNAMIC_OPEN"))// intent's action must be set
                )
                .build()
        //        ShortcutInfo dynamicShortcut4 = new ShortcutInfo.Builder(this, "shortcut_dynamic_4")
        //                .setShortLabel("Bike")
        //                .setLongLabel("Bike")
        //                .setIcon(Icon.createWithResource(this, R.drawable.ic_main_bike))
        //                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/life2015")))
        //                .build();

        // the max limit for shortcuts is 5 (static + dynamic), if we add more than 5, exception will be thrown
        // Caused by: java.lang.IllegalArgumentException: Max number of dynamic shortcuts exceeded


        shortcutManager.dynamicShortcuts = listOf(dynamicShortcut1, dynamicShortcut2, dynamicShortcut3)

    }
}