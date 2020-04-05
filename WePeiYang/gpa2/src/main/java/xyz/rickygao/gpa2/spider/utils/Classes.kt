package xyz.rickygao.gpa2.spider.utils

import android.content.Context
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
    fun init() {
        var okHttpClient = OkHttpClientGenerator.generate().build()
        var request = Request.Builder()
                .url(BASE_URL)
                .get()
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val headers = response.headers()

                var doc = Jsoup.parse(body?.string())
                var es = doc.select("input")
                for (e in es) {
                    if (e.attr("name") == "execution") {
                        execution = e.`val`()
                    }
                }
                ssoUrl = headers.get("Location")
            }
        })
    }

    /**
     * 登录
     */
    fun login(userName:String,password:String){
        userState.login(userName,password,execution)
    }

    /**
     * 登出
     */
    fun logout(){
        userState.logout()
    }
}