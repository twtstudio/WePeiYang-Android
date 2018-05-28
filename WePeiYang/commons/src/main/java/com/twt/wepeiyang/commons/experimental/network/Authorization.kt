package com.twt.wepeiyang.commons.experimental.network

import com.orhanobut.logger.Logger
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.hack.Restarter
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.service.AuthService
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
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
            if (response.request().isTrusted) {
                val code = JSONObject(response.body()?.string()).getInt("error_code")
                val relogin = fun(): Nothing {
                    launch(UI) {
                        AuthService.getToken(CommonPreferences.twtuname, CommonPreferences.password)
                                .awaitAndHandle {
                                    CommonContext.startActivity(name = "login")
                                }?.data?.token?.let { CommonPreferences.token = it }
                    }
                    throw IOException("登录失效，正在尝试自动重登")
                }
                when (code) {
                    10001 ->
                        if (response.priorResponse()?.request()?.header("Authorization") == null)
                            CommonPreferences.token
                        else relogin()
                    10003, 10004 -> relogin()
                //                        20001 -> Bind Tju
                    30001, 30002 -> {
                        val loggingIn = CommonContext.getActivity("login")
                                ?.isInstance(Restarter.getForegroundActivity(null))
                                ?: false
                        if (!loggingIn) {
                            CommonPreferences.isLogin = false
                            CommonContext.startActivity(name = "login")
                        }
                        throw IOException("帐号或密码错误")
                    }
                    else -> {
                        Logger.d("""
                                        Unhandled error code $code, for
                                        Request: ${response.request()}
                                        Response: $response
                                        """.trimIndent())
                        throw IOException("未知错误")
                    }
                }.let {
                    response.request().newBuilder()
                            .header("Authorization", "Bearer{$it}").build()
                }
            } else null
}

object CodeCorrectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
            chain.proceed(chain.request()).run {
                if (code() == HttpURLConnection.HTTP_BAD_REQUEST)
                    newBuilder().code(HttpURLConnection.HTTP_UNAUTHORIZED).build() else this
            }
}