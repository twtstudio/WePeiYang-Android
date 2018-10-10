package com.twt.service

import android.app.job.JobScheduler
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.twt.service.push.DebugProxyService
import com.twt.service.tjunet.reconnect.ReconnectJob
import com.twt.service.welcome.WelcomeActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.theme.CustomTheme
import com.twtstudio.retrox.auth.view.LoginActivity
import es.dmoral.toasty.Toasty
import io.multimoon.colorful.Defaults
import io.multimoon.colorful.initColorful

/**
 * Created by retrox on 2016/11/25.
 */

class WePeiYangApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        CommonContext.apply {
            registerApplication(this@WePeiYangApp)
            registerActivity("login", LoginActivity::class.java)
            registerActivity("welcome", WelcomeActivity::class.java)
        }

        Toasty.Config.getInstance()
                .setErrorColor(0xffAE837B.toInt())
                .setInfoColor(0xff738d91.toInt())
                .setSuccessColor(0xff748165.toInt())
                .setWarningColor(0xffD8BE71.toInt())
                .setTextColor(0xff242424.toInt())
                .apply()

        applicationContext.let {
            Bugly.init(it, "8ceee186f2", false)
            CrashReport.setAppChannel(it, "公测分发")
            CrashReport.setIsDevelopmentDevice(it, BuildConfig.DEBUG)
            BigImageViewer.initialize(GlideImageLoader.with(it))
        }

        val defaults: Defaults = Defaults(
                primaryColor = CustomTheme.customPeppaPink,
                accentColor = CustomTheme.customPeppaPink,
                useDarkTheme = false,
                translucent = false,
                customTheme = 0)
        initColorful(this, defaults)
        /**
         * emmmm 怎么弄得优雅一点？
         * 此处@高瑞均
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val jobinfo = ReconnectJob.getScheduler(this)
            val jobService = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobService.schedule(jobinfo)
        }

        /**
         * 用于调试API服务
         * adb forward tcp:10080 tcp:10086 然后在电脑本地使用Postman对该端口进行调试
         * example: $yourLocalPort = 10080
         * GET http://127.0.0.1:10080/api/v1/library/user/info
         * 不需要自己带token和sign
         * 暂不支持POST
         */
        if (BuildConfig.DEBUG) {
            try {
                val intent = Intent(this, DebugProxyService::class.java)
                startService(intent)
            } catch (e: Exception) {
                Log.e("DebugServer", "DebugApiServer 启动失败")
                e.printStackTrace()
            }
        }

    }
}
