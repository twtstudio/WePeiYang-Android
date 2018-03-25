package com.twt.wepeiyang.commons.experimental.network

import com.orhanobut.logger.Logger
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import okhttp3.*
import org.json.JSONObject
import java.net.HttpURLConnection

internal inline val Request.authorized
    get() = if (header("Authorization") == null)
        newBuilder().addHeader("Authorization", "Bearer{${CommonPreferences.token}}").build()
    else this

object AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
            chain.proceed(chain.request().authorized)
}

object RealAuthenticator : Authenticator {
    override fun authenticate(route: Route, response: Response): Request? =
            if (response.request().isTrusted)
                JSONObject(response.body()?.string()).getInt("error_code").let {
                    when (it) {
                        10001 ->
                            CommonPreferences.token
//                        10003 -> doesn't work?
//                            runBlocking {
//                                RealAuthService.refreshToken().await()
//                            }.data?.token?.let {
//                                CommonPreferences.token = it
//                                it
//                            }
                        10003, 10004 -> {
                            CommonPreferences.isLogin = false
                            CommonContext.startActivity(name = "login")
                            null
                        }
//                        20001 -> Bind Tju
                        else -> {
                            Logger.d("""
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
    override fun intercept(chain: Interceptor.Chain): Response =
            chain.proceed(chain.request()).run {
                if (code() == HttpURLConnection.HTTP_BAD_REQUEST)
                    newBuilder().code(HttpURLConnection.HTTP_UNAUTHORIZED).build() else this
            }
}