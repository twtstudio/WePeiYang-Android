package com.twtstudio.retrox.auth.api

import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal const val TRUSTED_HOST = "api.twt.edu.cn"
internal const val BASE_URL = "https://$TRUSTED_HOST/"


val ticket = "YmFuYW5hLjM3YjU5MDA2M2Q1OTM3MTY0MDVhMmM1YTM4MmIxMTMwYjI4YmY4YTc="


object AuthServiceFactory {
    private val loggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }

    //const val BASE_URL = "http://47.93.253.240:10805/api/user/"

    val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(false)
            .addInterceptor(CookieInterceptor)
            .addInterceptor(AuthorizationInterceptor)
            .addNetworkInterceptor(HeaderInterceptor)
            .addNetworkInterceptor(loggingInterceptor)
            .build()

    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    inline operator fun <reified T> invoke(): T = AuthServiceFactory.retrofit.create(T::class.java)

}

internal inline val Request.authorized
    get() = if (header("token") == null)
        newBuilder().addHeader("token", CommonPreferences.token).build()
    else this

object AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
            chain.proceed(chain.request().authorized)
}

//object RealAuthenticator : Authenticator {
//    override fun authenticate(route: Route?, response: Response): Request? {
//        val responseBodyCopy = response.peekBody(Long.MAX_VALUE) // 避免responseBody被一次性清空
//        val request = if (response.request().isTrusted) {
//            val code = JSONObject(responseBodyCopy?.string()).getInt("error_code")
//            val relogin = fun(): Nothing {
//                GlobalScope.launch(Dispatchers.Main) {
//                    AuthService.getToken(CommonPreferences.twtuname, CommonPreferences.password)
//                            .awaitAndHandle {
//                                CommonContext.startActivity(name = "login")
//                            }?.data?.token?.let { CommonPreferences.token = it }
//                }
//                throw IOException("登录失效，正在尝试自动重登")
//            }
//            when (code) {
//                10001 ->
//                    if (response.priorResponse()?.request()?.header("Authorization") == null)
//                        CommonPreferences.token
//                    else relogin()
//                10003, 10004 -> relogin()
//                40011 -> {
//                    CommonContext.startActivity(name = "bind") {
//                        putExtra("type", 0xfaee01) // in SingleBindActivity
//                        putExtra("message", "办公网绑定错误，请重新绑定办公网")
//                    }
//                    throw IOException("办公网帐号或密码错误")
//                }
//                30001, 30002 -> {
//                    val loggingIn = CommonContext.getActivity("login")
//                            ?.isInstance(Restarter.getForegroundActivity(null))
//                            ?: false
//                    if (!loggingIn) {
//                        CommonPreferences.isLogin = false
//                        CommonContext.startActivity(name = "login")
//                    }
//                    throw IOException("帐号或密码错误")
//                }
//                23000 -> {
//                    throw IOException("服务器数据库错误")
//                }
//                else -> {
//                    Logger.d("""
//                                        Unhandled error code $code, for
//                                        Request: ${response.request()}
//                                        Response: $response
//                                        """.trimIndent())
//                    return null // 交给外界处理 不要走Authenticator
//                }
//            }.let {
//                response.request().newBuilder()
//                        .header("Authorization", "Bearer{$it}").build()
//            }
//        } else null
//        return request
//    }
//}

internal object HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        builder.addHeader("ticket", ticket)
                .addHeader("domain", "weipeiyang.twt.edu.cn")
        var strigBuilder:StringBuilder ? = StringBuilder()
        Hawk.get<MutableList<String>>(CookieInterceptor.cookie, mutableListOf())  //取出上一步中存储的Cookie
                .run {
                    if (isNotEmpty()) {
                        forEach {
                            strigBuilder?.append(it)!!.append(";")
                        }
                        strigBuilder?.replace(strigBuilder.length,strigBuilder.length+1,"")//替换掉最后一个";"
                        builder.addHeader("Cookie", strigBuilder.toString())
                    }
                }
        val request = builder.build()
        return chain.proceed(request)
    }
}

internal object CookieInterceptor : Interceptor {

    const val cookie:String = "set-cookie"

    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain?.request()?:return null
        val response = chain.proceed(request)
        val cookies  = mutableListOf<String>()
        response.headers("set-cookie").run {
            if (isNotEmpty()){
                forEach {
                    cookies.add(it)
                }
            }
        }
        Hawk.put(cookie,cookies)//本地持久化，过期问题此处没处理
        return response
    }
}