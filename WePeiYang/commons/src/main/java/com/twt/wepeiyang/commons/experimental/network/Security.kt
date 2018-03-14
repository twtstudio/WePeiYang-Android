package com.twt.wepeiyang.commons.experimental.network

import okhttp3.Interceptor
import okhttp3.Request

internal inline val Request.isTrusted
    get() = url().host() == ServiceFactory.TRUSTED_HOST

/**
 * A wrapped interceptor only applied to trusted request.
 *
 * @see ServiceFactory
 */
internal val Interceptor.forTrusted
    get() = Interceptor {
        if (it.request().isTrusted) intercept(it)
        else it.proceed(it.request())
    }