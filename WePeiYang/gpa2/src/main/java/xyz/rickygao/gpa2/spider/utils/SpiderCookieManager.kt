package xyz.rickygao.gpa2.spider.utils

import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.startActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 生成爬虫所需的okHttpClient
 * 设置 CookieJar 在发送请求前设置cookie，接收请求后保存cookie
 */
object SpiderCookieManager {

//    const val DIALOG_ACTION = "gpa2.spider.utils.dialog"
    val cookieStore = PersistentCookieStore(CommonContext.application)
    private val cookieJar = CookieJarImpl(cookieStore)
    val clientBuilder = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addNetworkInterceptor(HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY })

//    private val localBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(CommonContext.application)

//    init {
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(DIALOG_ACTION)
//        val localReceiver = TjuLoginDialogReceiver()
//        localBroadcastManager.registerReceiver(localReceiver, intentFilter)
//    }

    /**
     * 构造爬虫所需 OkHttpClient.Builder
     * 设置了cookieJar 缓存cookie
     * 并对cookie是否过期进行判断
     */
    suspend fun getClientBuilder(): OkHttpClient.Builder {
        printCookie()
        if (CommonPreferences.tjuloginbind) {
            // 曾经成功登录办公网，账户密码依然可以继续使用
            if (cookieStore.cookies.isNotEmpty()) {
                Log.d("SpiderCookieApi", "has cookies")
                // 如果有cookie过期就重新登录
                for (cookie in cookieStore.cookies) {
                    if (cookie.isExpired()) {
                        Log.d("SpiderCookieApi", "expired ${cookie.name()}")
                        GlobalScope.launch(Main) {

                            Toasty.info(CommonContext.application, "办公网登录已过期，更新课表、GPA等信息请重新登录", Toast.LENGTH_LONG).show()

                        }
//                        checkTjuValid(SpiderTjuLogin.login(CommonPreferences.tjuuname, CommonPreferences.tjupwd))
                        break
                    }
                }
                return clientBuilder
            }
        }
        // 未曾成功登录过，需要登录
        Log.d("SpiderCookieApi", "never login")
        GlobalScope.launch(Main) {
//            val intent = Intent(DIALOG_ACTION)
//            localBroadcastManager.sendBroadcast(intent)
//            CommonContext.application.sendBroadcast(intent)
//            throw NotLoginException("尚未登录办公网")
            Toasty.warning(CommonContext.application, "尚未登录办公网").show()
        }
//        printCookie()
        return clientBuilder
    }

//    private fun checkTjuValid(valid: Boolean) {
//        if (!valid) {
//            CommonPreferences.tjuloginbind = false
////            Toast.makeText(CommonContext.application,"办公网重新绑定（最近更换密码）",Toast.LENGTH_LONG).show()
//            CommonContext.application.startActivity("bind") {
//                // module app 中的com.twt.service.settings.SingleBindActivity
//                val TJU_BIND = 0xfaee01
//                val TYPE = "type"
//                this.putExtra(TYPE, TJU_BIND)
//                this.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//            }
//        } else {
//            CommonPreferences.tjuloginbind = true
//        }
//    }

    fun clearCookie() {
        cookieStore.removeAll()
    }

    private fun printCookie() {
        Log.d("SpiderCookieApi", cookieStore.cookies.toString())
        for (cookie in cookieStore.cookies) {
            Log.d("SpiderCookieApi", "detail: $cookie")
        }
    }
}

