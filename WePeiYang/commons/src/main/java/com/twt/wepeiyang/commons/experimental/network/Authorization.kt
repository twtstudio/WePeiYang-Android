package com.twt.wepeiyang.commons.experimental.network

import com.orhanobut.logger.Logger
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.preferences.CommonPreferences
import com.twt.wepeiyang.commons.experimental.service.RealAuthService
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.*
import org.json.JSONObject
import java.net.HttpURLConnection

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
    override fun authenticate(route: Route, response: Response): Request? =
            if (response.request().isTrusted)
                JSONObject(response.body()?.string()).getInt("error_code").let {
                    when (it) {
                        10001 ->
                            CommonPreferences.token
                        10003 ->
                            runBlocking {
                                RealAuthService.refreshToken().await()
                            }.data?.token?.let {
                                CommonPreferences.token = it
                                it
                            }
                        10004 -> {
                            CommonContext.startActivity(name = "login")
                            null
                        }
//                        20001 -> Bind Tju
                        else -> {
                            Logger.w("""
                                Unhandled error code $it, for
                                Request: ${response.request()}
                                Response: $response
                                """.trimIndent())
                            null
                        }
                    }
                }?.let {
                    response.request().newBuilder()
                            .header("Authorization", "Bearer{$it}").build()
                } else null
}

object CodeCorrectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request()).run {
        if (code() == HttpURLConnection.HTTP_BAD_REQUEST)
            newBuilder().code(HttpURLConnection.HTTP_UNAUTHORIZED).build() else this
    }
}