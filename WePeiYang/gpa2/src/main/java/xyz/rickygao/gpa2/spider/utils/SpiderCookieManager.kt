package xyz.rickygao.gpa2.spider.utils

import android.util.Log
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
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
    val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addNetworkInterceptor(HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY })


    /**
     * 构造爬虫所需 OkHttpClient.Builder
     * 设置了cookieJar 缓存cookie
     * 并对cookie是否过期进行判断
     */
    suspend fun getClientBuilder(): OkHttpClient.Builder {
        printCookie("getClientBuilder")
        return clientBuilder
    }

    suspend fun getLoginState(callback: suspend (RefreshState<Unit>) -> Unit) {
        // 清除过期cookie，如果有过期的，会在clearExpired里将tjulogin设置为false
        cookieStore.clearExpired()
        when (CommonPreferences.tjulogin) {
            false -> {
                callback(RefreshState.Failure(RuntimeException("办公网登录已过期，使用本地缓存数据")))
            }
            true -> {
                callback(RefreshState.Success(Unit))
            }
            else -> {
                callback(RefreshState.Failure(RuntimeException("未登录办公网，使用本地缓存数据")))
            }

        }

    }


    fun clearCookie() {
        cookieStore.removeAll()
    }

    private fun printCookie(tag: String) {
        Log.d("SpiderCookieApi", "$tag=======================================")
//        Log.d("SpiderCookieApi", cookieStore.cookies.toString())
        for (cookie in cookieStore.cookies) {
            Log.d("SpiderCookieApi", "detail: $cookie")
        }
    }
}
