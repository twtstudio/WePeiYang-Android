package com.twt.service.mall

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.webkit.*
import android.widget.Toast
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.text.url
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.webView

class MallActivity : AppCompatActivity() {
//    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var webView: WebView
    private val mallUrl = "https://mall.twt.edu.cn/api.php/Login/wpyLogin?model=2"
    private val headerKey = "Authorization"
    private val headerToken = "Bearer{${CommonPreferences.token}}"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val map = HashMap<String, String>()
        map[headerKey] = headerToken
        window.statusBarColor = Color.parseColor("#f58010")
//      swipeRefreshLayout = swipeRefreshLayout {
//      setColorSchemeColors(ContextCompat.getColor(this@MallActivity, R.color.colorMall))
            webView = webView {

                loadUrl(mallUrl, map)
                settings.apply {
                    javaScriptEnabled = true
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
//                        swipeRefreshLayout.isRefreshing = newProgress != 100
                    }
                }
                webViewClient = object : WebViewClient() {}
            }
//            onRefresh {
//                webView.loadUrl(mallUrl, map)
//            }
//        }
    }

    // 拦截 back 键，若非新闻网首页退回新闻网首页，若为新闻网首页结束此 activity
    override fun onBackPressed() {
        if (webView.url == "https://mall.twt.edu.cn/m/") {
            super.onBackPressed()
        } else {
            val map = HashMap<String, String>()
            map[headerKey] = headerToken
            webView.loadUrl(mallUrl,map)
        }
    }
}