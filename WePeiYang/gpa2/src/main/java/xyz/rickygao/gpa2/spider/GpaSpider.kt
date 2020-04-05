package xyz.rickygao.gpa2.spider

import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import xyz.rickygao.gpa2.spider.cookie.OkHttpClientGenerator
import xyz.rickygao.gpa2.spider.utils.Classes
import java.io.IOException

class GpaSpider {
    private fun login(tjuUName: String, tjuPassword: String) {
        val classes = Classes()
        classes.init()
        classes.login(tjuUName, tjuPassword)
    }

    fun getGpa(tjuUName: String, tjuPassword: String, semesterId: String): Deferred<String?> {

        login(tjuUName, tjuPassword)
        val okHttpClient = OkHttpClientGenerator.generate().build()
        val request = Request.Builder()
                .url("http://classes.tju.edu.cn/eams/teach/grade/course/person!search.action?semesterId=$semesterId&_=${System.currentTimeMillis()}")
                .get()
                .build()
        return GlobalScope.async {
            okHttpClient.newCall(request).execute().body()?.string()
        }
    }
}