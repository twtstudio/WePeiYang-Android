package xyz.rickygao.gpa2.spider.utils

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.startActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 生成爬虫所需的okHttpClient
 * 设置 CookieJar 在发送请求前设置cookie，接收请求后保存cookie
 */
object SpiderCookieManager {

    val cookieStore = PersistentCookieStore(CommonContext.application)
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
//        printCookie()
        if (CommonPreferences.tjuloginbind) {
            // 曾经成功登录办公网，账户密码依然可以继续使用
            if (cookieStore.cookies.isNotEmpty()) {
                // 如果有cookie过期就重新登录
                for (cookie in cookieStore.cookies) {
                    if (cookie.isExpired()) {
                        Log.d("SpiderCookieApi", "expired ${cookie.name()}")
                        GlobalScope.launch(Main) {

                            Toasty.info(CommonContext.application, "办公网登录已过期").show()

                        }
//                        checkTjuValid(SpiderTjuLogin.login(CommonPreferences.tjuuname, CommonPreferences.tjupwd))
                        break
                    }
                }
                return clientBuilder
            }
        }
        // 未曾成功登录过，需要登录
        Log.d("SpiderCookieApi", "never login")
        GlobalScope.launch(Main) {

            Toasty.warning(CommonContext.application, "尚未登录办公网").show()
        }
//        checkTjuValid(SpiderTjuLogin.login(CommonPreferences.tjuuname, CommonPreferences.tjupwd))
//        printCookie()
        return clientBuilder
    }

    private fun checkTjuValid(valid: Boolean) {
        if (!valid) {
            CommonPreferences.tjuloginbind = false
//            Toast.makeText(CommonContext.application,"办公网重新绑定（最近更换密码）",Toast.LENGTH_LONG).show()
            CommonContext.application.startActivity("bind") {
                // module app 中的com.twt.service.settings.SingleBindActivity
                val TJU_BIND = 0xfaee01
                val TYPE = "type"
                this.putExtra(TYPE, TJU_BIND)
                this.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        } else {
            CommonPreferences.tjuloginbind = true
        }
    }

    fun clearCookie() {
        cookieStore.removeAll()
    }

    fun printCookie() {
        Log.d("SpiderCookieApi", cookieStore.cookies.toString())
        for (cookie in cookieStore.cookies) {
            Log.d("SpiderCookieApi", "detail: $cookie")
        }
    }
}

