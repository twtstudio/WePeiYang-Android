package com.twt.wepeiyang.commons.experimental.network

import okhttp3.Interceptor
import okhttp3.Request

internal inline val Request.isTrusted: Boolean
    get() = url().host() == ServiceFactory.TRUSTED_HOST

/**
 * A wrapped interceptor only applied to isTrusted request.
 *
 * @see ServiceFactory
 */
internal inline val Interceptor.forTrusted: Interceptor
    get() = Interceptor {
        if (it.request().isTrusted) intercept(it)
        else it.proceed(it.request())
    }