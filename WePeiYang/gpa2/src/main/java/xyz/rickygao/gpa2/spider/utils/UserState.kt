package xyz.rickygao.gpa2.spider.utils

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.jsoup.Jsoup
import xyz.rickygao.gpa2.spider.cookie.OkHttpClientGenerator
import java.io.IOException

/**
 * 登录 登出
 */
object UserState{
    private val BASE_URL = "https://sso.tju.edu.cn/cas/login"
    private val url = "$BASE_URL?service=http://classes.tju.edu.cn/eams/homeExt.action"
    private val logoutUrl = "http://classes.tju.edu.cn/eams/logoutExt.action"
    suspend fun login(userName: String, password: String, execution: String) {
        var okHttpClient = OkHttpClientGenerator.generate().build()
        var requestBody = FormBody.Builder()
                .add("username", userName)
                .add("password", password)
                .add("_eventId", "submit")
                .add("execution", execution)
                .build()
        var request = Request.Builder().url(url).post(requestBody).build()
        okHttpClient.newCall(request).execute()
    }


    fun logout() {
        GlobalScope.launch {
            var doc = Jsoup.connect(logoutUrl).get()
            Log.d("logout", doc.toString())
        }
    }
}