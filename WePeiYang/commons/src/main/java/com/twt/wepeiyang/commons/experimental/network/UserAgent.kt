package com.twt.wepeiyang.commons.experimental.network

import android.os.Build
import com.twt.wepeiyang.commons.experimental.CommonContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * User-Agent's format: "$ApplicationName/$ApplicationVersion($Device; $OS $OSVersion)"
 */
internal val userAgent = "WePeiYang/${CommonContext.applicationVersion} (${Build.BRAND} ${Build.PRODUCT}; Android ${Build.VERSION.SDK_INT})"

internal val Request.uaed: Request
    get() = newBuilder().header("User-Agent", userAgent).build()

internal object UserAgentInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request().uaed)
}