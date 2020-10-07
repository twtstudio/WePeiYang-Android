package xyz.rickygao.gpa2.spider.utils

import android.util.Log
import okhttp3.Cookie
import okhttp3.FormBody
import okhttp3.Request
import org.jsoup.Jsoup

/**
 * https://sso.tju.edu.cn/cas/login
 * 通过爬虫登录办公网
 * execution为登录必填项，每次登录会发生改变。访问上面的网址可以得到html文件，

==== html 部分内容如下 ====
<section class="row btn-row">
<input type="hidden" name="execution" value="e7s1"/>
==========================
 *
 * session 在第一次访问上面的网址时可以在response header 中的Set-Cookiew中得到，请求验证码和登录时都要携带
 * cookie 的使用与存储通过manager管理
 * @see SpiderCookieManager
 * 使用该类获取clientBuilder来发送请求可以拦截请求，在发送的请求头上加cookie，并存储收到的请求的cookie
 *
 */
object SpiderTjuApi {
    //    private val COOKIE_BASE_URL = "http://classes.tju.edu.cn/eams/homeExt.action"

    private var execution = ""
    private var session: Cookie? = null
    val KAPTCHA_URL = "https://sso.tju.edu.cn/cas/images/kaptcha.jpg"
    private val LOGIN_URL = "https://sso.tju.edu.cn/cas/login"

    //    private val loginUrl = "$LOGIN_BASE_URL?service=http://classes.tju.edu.cn/eams/homeExt.action"
    private val logoutUrl = "http://classes.tju.edu.cn/eams/logoutExt.action"

    /**
     * 必须在每次登录前调用
     *
     */
    suspend fun prepare() {
        // 获取 session
        // 解析登录所需参数 execution
        val requestInit = Request.Builder()
                .url(LOGIN_URL)
                .get()
                .build()
        val response = SpiderCookieManager.clientBuilder.build().newCall(requestInit).execute()
        val newCookie = response.header("Set-Cookie")
        val body = response.body()?.string().orEmpty()
        val doc = Jsoup.parse(body)
        val es = doc.select("input")
        for (e in es) {
            if (e.attr("name") == "execution") {
                execution = e.`val`()
                Log.d("Log execution", execution)
            }
        }
        Log.d("Log session", newCookie ?: "null")

//        return newCookie
        for (cookie in SpiderCookieManager.cookieStore.cookies) {
            if (cookie.name() == "SESSION") {
                session = cookie
            }
        }
    }

    /**
     *  获取cookie session. execution
     *  验证码的请求放在app模块中 com.twt.service.settings.SingleBindActivity，使用glide将验证码图片加载
     *
     */
    suspend fun getSession(): String? {
        if (session?.isExpired() != false) {
            prepare()
        }
        return session.toString()

    }

    /**
     * 登录
     */
    suspend fun login(userName: String, password: String, captcha: String): Int {

        /*
         * 登录
         */
        var requestBody = FormBody.Builder()
                .add("username", userName)
                .add("password", password)
                .add("_eventId", "submit")
                .add("execution", execution)
                .add("captcha", captcha)
                .build()
        var requestLogin = Request.Builder().url(LOGIN_URL)
                .post(requestBody).build()

        val response = SpiderCookieManager.clientBuilder.build().newCall(requestLogin).execute()
//        val loginBody = response.body()?.string().orEmpty()
//        refreshCookie()
//        printCookie()
        return response.code()

    }


    /**
     * 登出
     */
    fun logout() {
        var doc = Jsoup.connect(logoutUrl).get()
        Log.d("logout", doc.toString())
    }
//
//    private fun refreshCookie() {
//        cookieJar = CookieJarImpl(PersistentCookieStore(CommonContext.application))
//    }
//
//    private fun printCookie() {
//        Log.d("SpiderCookieLogin", cookieJar.cookieStore.cookies.toString())
//    }
}