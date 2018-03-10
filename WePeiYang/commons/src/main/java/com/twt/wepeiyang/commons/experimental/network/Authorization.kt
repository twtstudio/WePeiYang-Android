package com.twt.wepeiyang.commons.experimental.network

import com.twt.wepeiyang.commons.experimental.Commons
import com.twt.wepeiyang.commons.utils.CommonPreferences
import okhttp3.*
import org.json.JSONObject
import java.net.HttpURLConnection

/**
 * Created by rickygao on 2018/3/9.
 */
object AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = if (chain.request().header("Authorization") == null)
            chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer{${CommonPreferences.token}}")
                    .build() else chain.request()
        return chain.proceed(request)
    }
}

object RealAuthenticator : Authenticator {
    override fun authenticate(route: Route, response: Response): Request? {
        if (response.request().isTrusted) {
            val err = JSONObject(response.body()?.string()).run { getInt("error_code") }
            when (err) {
                10001 ->
                    return response.request().newBuilder().header("Authorization", "Bearer{${CommonPreferences.token}}").build()
                10003 ->
                    RealAuthService.refreshToken().execute().body()?.data?.token?.let {
                        CommonPreferences.token = it
                        return response.request().newBuilder().header("Authorization", "Bearer{${it}}").build()
                    }
                10004 ->
                    Commons.startLoginActivity()
            }
        }
        return null
    }
}

object CodeCorrectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request()).let {
        if (it.code() == HttpURLConnection.HTTP_BAD_REQUEST)
            it.newBuilder().code(HttpURLConnection.HTTP_UNAUTHORIZED).build() else it
    }
}