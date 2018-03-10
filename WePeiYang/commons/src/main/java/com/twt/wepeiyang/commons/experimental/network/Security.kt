package com.twt.wepeiyang.commons.experimental.network

import okhttp3.Interceptor
import okhttp3.Request

/**
 * Created by rickygao on 2018/3/10.
 */
internal val Request.trusted: Boolean
    get() = url().host() == ServiceFactory.TRUSTED_HOST

internal val Interceptor.forTrusted: Interceptor
    get() = Interceptor {
        if (it.request().trusted) intercept(it)
        else it.proceed(it.request())
    }