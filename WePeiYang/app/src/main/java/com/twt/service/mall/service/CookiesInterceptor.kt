package com.twt.service.mall.service

import android.content.Context
import android.content.SharedPreferences
import android.preference.Preference
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ReceivedCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originResponse: Response = chain.proceed(chain.request())
        if (originResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies: HashSet<String> = HashSet()
            for (it in originResponse.headers("Set-Cookie")) {
                cookies.add(it)
            }
        }
        return originResponse
    }
}

class AddCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
                .addHeader("Cookie", Utils.getCookie())
        return chain.proceed(builder.build())
    }
}