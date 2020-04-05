package xyz.rickygao.gpa2.spider

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import xyz.rickygao.gpa2.spider.cookie.CookieJarImpl
import xyz.rickygao.gpa2.spider.cookie.PersistentCookieStore

object OkHttpClientGenerator {

    fun generate(context:Context):OkHttpClient{
        val loggingInterceptor = HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
                .cookieJar(CookieJarImpl(PersistentCookieStore(context)))
                .addNetworkInterceptor(loggingInterceptor).build()
    }
}