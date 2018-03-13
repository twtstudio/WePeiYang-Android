package com.twt.service

import android.support.multidex.MultiDexApplication
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.twt.service.welcome.WelcomeActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twtstudio.retrox.auth.view.LoginActivity

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

        applicationContext.let {
            Bugly.init(it, "8ceee186f2", false)
            CrashReport.setAppChannel(it, "公测分发")
            CrashReport.setIsDevelopmentDevice(it, BuildConfig.DEBUG)
            BigImageViewer.initialize(GlideImageLoader.with(it))
        }

    }
}
