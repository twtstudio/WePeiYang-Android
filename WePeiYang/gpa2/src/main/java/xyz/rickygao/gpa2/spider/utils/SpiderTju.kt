package xyz.rickygao.gpa2.spider.utils

import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import okhttp3.Cookie
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 生成爬虫所需的okHttpClient
 * 设置 CookieJar 在发送请求前设置cookie，接收请求后保存cookie
 */
object SpiderTju {

    private val cookieJar = CookieJarImpl(PersistentCookieStore(CommonContext.application))
    private val clientBuilder = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addNetworkInterceptor(HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY })

    /**
     * 构造爬虫所需 OkHttpClient.Builder
     * 设置了cookieJar 缓存cookie
     * 并对cookie是否过期进行判断
     */
    suspend fun getClientBuilder(): OkHttpClient.Builder {
        val cookies = cookieJar.cookieStore.cookies
        cookies?.let {
            for (cookie in it) {
                if (!cookie.isExpired()) {
                    checkTjuValid(SpiderTjuLogin.login(CommonPreferences.tjuuname, CommonPreferences.tjupwd))
                    return clientBuilder
                }
            }
        }

        checkTjuValid(SpiderTjuLogin.login(CommonPreferences.tjuuname, CommonPreferences.tjupwd))
        return clientBuilder
    }

    private fun checkTjuValid(valid: Boolean) {
        if (!valid) {
            //TODO 启动绑定办公网
        }
    }

    fun clear() {
        cookieJar.cookieStore.removeAll()
    }
}

