package xyz.rickygao.gpa2.spider.utils

import android.util.Log
import okhttp3.FormBody
import okhttp3.Request
import org.jsoup.Jsoup

/**
 * classes.tju.edu.cn
 * cookie 获取
 *
 */
object SpiderTjuApi {
    //    private val COOKIE_BASE_URL = "http://classes.tju.edu.cn/eams/homeExt.action"
    private var execution = ""

    private val KAPTCHA_URL = "https://sso.tju.edu.cn/cas/images/kaptcha.jpg"
    private val LOGIN_URL = "https://sso.tju.edu.cn/cas/login"

    //    private val loginUrl = "$LOGIN_BASE_URL?service=http://classes.tju.edu.cn/eams/homeExt.action"
    private val logoutUrl = "http://classes.tju.edu.cn/eams/logoutExt.action"

//    private var cookieJar = CookieJarImpl(PersistentCookieStore(CommonContext.application))
//    private val okHttpClient = OkHttpClient.Builder()
//            .cookieJar(cookieJar)
//            .addNetworkInterceptor(HttpLoggingInterceptor()
//                    .apply { level = HttpLoggingInterceptor.Level.BODY }).build()

    suspend fun getSession():String?{
        /*
         *
         * 获取 session
         * 解析登录所需参数 execution
         */
        val requestInit = Request.Builder()
                .url(LOGIN_URL)
                .get()
                .build()
        val body = SpiderCookieManager.clientBuilder.build().newCall(requestInit).execute().body()?.string().orEmpty()
        val doc = Jsoup.parse(body)
        val es = doc.select("input")
        for (e in es) {
            if (e.attr("name") == "execution") {
                execution = e.`val`()
            }
        }
        for(cookie in SpiderCookieManager.cookieStore.cookies){
            if(cookie.name() == "SESSION"){
                return cookie.value()
            }
        }
        return null
    }
    /**
     * 登录
     * 无论登录是否成功，都会获取cookie session
     */
    suspend fun login(userName: String, password: String, captcha: String): Boolean {

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
                .addHeader("Accept-Language", "en-US").post(requestBody).build()

        val loginBody = SpiderCookieManager.clientBuilder.build().newCall(requestLogin).execute().body()?.string().orEmpty()
//        refreshCookie()
//        printCookie()
        return !(loginBody.contains("Invalid credentials") || userName.trim() == "" || password.trim() == "")
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