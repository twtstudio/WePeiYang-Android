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
import com.tencent.mta.track.StatisticsDataAPI
import com.tencent.stat.StatService
import com.twt.service.push.DebugProxyService
import com.twt.service.schedule2.view.theme.ScheduleTheme
import com.twt.service.settings.PrivacyActivity
import com.twt.service.settings.SingleBindActivity
import com.twt.service.tjunet.reconnect.ReconnectJob
import com.twt.service.welcome.WelcomeActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.theme.CustomTheme
import com.twtstudio.retrox.auth.view.LoginActivity
import io.multimoon.colorful.Colorful
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
            registerActivity("bind", SingleBindActivity::class.java)
            registerActivity("privacy", PrivacyActivity::class.java)
        }

        StatService.registerActivityLifecycleCallbacks(this)
        StatisticsDataAPI.instance(this);

        applicationContext.let {
            Bugly.init(it, "8ceee186f2", false)
            CrashReport.setAppChannel(it, "公测分发")
            CrashReport.setIsDevelopmentDevice(it, BuildConfig.DEBUG)
            BigImageViewer.initialize(GlideImageLoader.with(it))
        }

        applyTheme()

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

    private fun applyTheme() {
        try {
            val themeIndex = CommonPreferences.customThemeIndex
            if (themeIndex < 5) {
                ScheduleTheme.setCurrentTheme("Lex")
            } else {
                ScheduleTheme.setCurrentTheme("Pink")
            }

            val theme = CustomTheme.themeList[themeIndex]
            val defaults: Defaults = Defaults(
                    primaryColor = theme,
                    accentColor = theme,
                    useDarkTheme = false,
                    translucent = false,
                    customTheme = 0)

            initColorful(this, defaults)
            Colorful().edit().resetPrefs(this)
            Colorful().edit()
                    .setPrimaryColor(theme)
                    .setAccentColor(theme).apply(this)
        } catch (e: Exception) {
            e.printStackTrace()
            val defaults: Defaults = Defaults(
                    primaryColor = CustomTheme.customPollia,
                    accentColor = CustomTheme.customPollia,
                    useDarkTheme = false,
                    translucent = false,
                    customTheme = 0)

            initColorful(this, defaults)
        }
    }
}
