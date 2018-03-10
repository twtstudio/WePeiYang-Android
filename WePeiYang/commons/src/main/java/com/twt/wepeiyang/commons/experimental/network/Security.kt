package com.twt.wepeiyang.commons.experimental.network

import okhttp3.Interceptor
import okhttp3.Request

internal val Request.trusted: Boolean
    get() = url().host() == ServiceFactory.TRUSTED_HOST

/**
 * A wrapped interceptor only applied to trusted request.
 *
 * @see ServiceFactory
 */
internal val Interceptor.forTrusted: Interceptor
    get() = Interceptor {
        if (it.request().trusted) intercept(it)
        else it.proceed(it.request())
    }