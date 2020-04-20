package xyz.rickygao.gpa2.spider.utils

import android.util.Log
import com.twt.wepeiyang.commons.experimental.CommonContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import java.util.*

/**
 * classes.tju.edu.cn
 * cookie 获取
 *
 */
object SpiderTjuLogin {
    private val COOKIE_BASE_URL = "http://classes.tju.edu.cn/eams/homeExt.action"
    private var execution = ""

    private val LOGIN_BASE_URL = "https://sso.tju.edu.cn/cas/login"
    private val loginUrl = "$LOGIN_BASE_URL?service=http://classes.tju.edu.cn/eams/homeExt.action"
    private val logoutUrl = "http://classes.tju.edu.cn/eams/logoutExt.action"
    private val cookieJar = CookieJarImpl(PersistentCookieStore(CommonContext.application))
    private val okHttpClient = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addNetworkInterceptor(HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY }).build()

    /**
     * 登录
     */
    suspend fun login(userName: String, password: String): Boolean {
        /*
         *
         * 获取 session
         * 解析登录所需参数 execution
         */
        val requestInit = Request.Builder()
                .url(COOKIE_BASE_URL)
                .get()
                .build()
        val body = okHttpClient.newCall(requestInit).execute().body()?.string().orEmpty()
        val doc = Jsoup.parse(body)
        val es = doc.select("input")
        for (e in es) {
            if (e.attr("name") == "execution") {
                execution = e.`val`()
            }
        }
        /*
         * 登录
         */
        var requestBody = FormBody.Builder()
                .add("username", userName)
                .add("password", password)
                .add("_eventId", "submit")
                .add("execution", execution)
                .build()
        var requestLogin = Request.Builder().url(loginUrl)
                .addHeader("Accept-Language", "en-US").post(requestBody).build()
        val loginBody = okHttpClient.newCall(requestLogin).execute().body()?.string().orEmpty()
        printCookie()
        return !(loginBody.contains("Invalid credentials") || userName.trim() == "" || password.trim() == "")
    }


    /**
     * 登出
     */
    fun logout() {
        var doc = Jsoup.connect(logoutUrl).get()
        Log.d("logout", doc.toString())
    }
    private fun printCookie(){
        Log.d("SpiderCookieLogin", cookieJar.cookieStore.cookies.toString())
    }
}