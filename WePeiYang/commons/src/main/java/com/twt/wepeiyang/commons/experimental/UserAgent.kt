package com.twt.wepeiyang.commons.experimental

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by rickygao on 2018/3/5.
 */

fun getApplicationVersion() = Commons.applicationContext.packageManager.getPackageInfo(Commons.applicationContext.packageName, 0).versionName

// ApplicationName/ApplicationVersion(DeviceModel; OS OSVersion)
internal fun generateUserAgent() = "WePeiYang/${getApplicationVersion()} (${Build.BRAND} ${Build.MODEL}; Android ${Build.VERSION.SDK_INT})"

internal val Request.uaed: Request
    get() = newBuilder().removeHeader("UserAgent").addHeader("UserAgent", generateUserAgent()).build()

internal object UserAgentInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
            chain.proceed(if (chain.request().trusted) chain.request().uaed else chain.request())
}
