package xyz.rickygao.gpa2.spider.utils

import android.content.Intent
import android.util.Log
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.startActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 生成爬虫所需的okHttpClient
 * 设置 CookieJar 在发送请求前设置cookie，接收请求后保存cookie
 */
object SpiderTjuApi {

    private val cookieStore = PersistentCookieStore(CommonContext.application)
    private val cookieJar = CookieJarImpl(cookieStore)
    val clientBuilder = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addNetworkInterceptor(HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY })

    /**
     * 构造爬虫所需 OkHttpClient.Builder
     * 设置了cookieJar 缓存cookie
     * 并对cookie是否过期进行判断
     */
    suspend fun getClientBuilder(): OkHttpClient.Builder {
        printCookie()
        cookieStore.cookies?.let {
            for (cookie in it) {
                if (!cookie.isExpired()) {
                    Log.d("SpiderCookieApi", "expired")
                    checkTjuValid(SpiderTjuLogin.login(CommonPreferences.tjuuname, CommonPreferences.tjupwd))
                    return clientBuilder
                }
            }
        }
        Log.d("SpiderCookieApi", "no cookie")
        checkTjuValid(SpiderTjuLogin.login(CommonPreferences.tjuuname, CommonPreferences.tjupwd))
//        refreshCookie()
        printCookie()
        return clientBuilder
    }

    private fun checkTjuValid(valid: Boolean) {
        if (!valid) {
//            Toast.makeText(CommonContext.application,"办公网重新绑定（最近更换密码）",Toast.LENGTH_LONG).show()
            CommonContext.application.startActivity("bind") {
                // module app 中的com.twt.service.settings.SingleBindActivity
                val TJU_BIND = 0xfaee01
                val TYPE = "type"
                this.putExtra(TYPE, TJU_BIND)
                this.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        }
    }
//    private fun refreshCookie(){
//        cookieJar = CookieJarImpl(PersistentCookieStore(CommonContext.application))
//    }
    fun clear() {
        cookieStore.removeAll()
    }
    fun printCookie(){
        Log.d("SpiderCookieApi", cookieStore.cookies.toString())
    }
}

