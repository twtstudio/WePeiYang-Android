package xyz.rickygao.gpa2.spider.cookie

import com.twt.wepeiyang.commons.experimental.CommonContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 生成爬虫所需的okHttpClient
 * 设置 CookieJar 在发送请求前设置cookie，接收请求后保存cookie
 */
object OkHttpClientGenerator {

    private val cookieStore = CookieJarImpl(PersistentCookieStore(CommonContext.application))
    fun generate():OkHttpClient.Builder{
        val loggingInterceptor = HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
                .cookieJar(cookieStore)
                .addNetworkInterceptor(loggingInterceptor)
    }
    fun clear(){
        cookieStore.cookieStore.removeAll()
    }
}