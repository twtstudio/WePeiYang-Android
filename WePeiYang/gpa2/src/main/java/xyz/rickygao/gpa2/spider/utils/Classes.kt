package xyz.rickygao.gpa2.spider.utils

import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import xyz.rickygao.gpa2.spider.cookie.OkHttpClientGenerator
import java.io.IOException

/**
 * classes.tju.edu.cn
 * cookie 获取
 *
 */
public class Classes() {
    private val BASE_URL = "http://classes.tju.edu.cn/eams/homeExt.action"
    private var ssoUrl: String? = ""

    private var execution = ""
    private var userState: UserState = UserState()

    /**
     * 登录前调用。
     * 获取 session
     * 解析登录所需参数 execution
     */
    suspend fun init() {
        var okHttpClient = OkHttpClientGenerator.generate().build()
        var request = Request.Builder()
                .url(BASE_URL)
                .get()
                .build()
        val body = okHttpClient.newCall(request).execute().body()?.string()


        var doc = Jsoup.parse(body)
        var es = doc.select("input")
        for (e in es) {
            if (e.attr("name") == "execution") {
                execution = e.`val`()
            }
        }


    }

    /**
     * 登录
     */
    suspend fun login(userName: String, password: String) {
        userState.login(userName, password, execution)
    }

    /**
     * 登出
     */
    fun logout() {
        userState.logout()
    }
}