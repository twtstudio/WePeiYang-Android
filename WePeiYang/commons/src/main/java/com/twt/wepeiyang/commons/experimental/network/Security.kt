package com.twt.wepeiyang.commons.experimental.network

import okhttp3.Interceptor
import okhttp3.Request

internal inline val Request.isTrusted
    get() = url().host() in trustedHosts

private val trustedHosts = setOf("open.twtstudio.com", "exam.twtstudio.com", "open-lostfound.twtstudio.com","open.twt.edu.cn")
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
