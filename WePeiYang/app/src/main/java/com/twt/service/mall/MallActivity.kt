package com.twt.service.mall

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
    private lateinit var webView: WebView
    private val mallUrl = "https://mall.twt.edu.cn/api.php/Login/wpyLogin?model=2"
    private val headerKey = "Authorization"
    private val headerToken = "Bearer{${CommonPreferences.token}}"
    private var historyStack = mutableListOf<String>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val map = HashMap<String, String>()
        map[headerKey] = headerToken
        window.statusBarColor = Color.parseColor("#f58010")
        webView = webView {
            loadUrl(mallUrl, map)
            settings.apply {
                javaScriptEnabled = true
            }
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String) {
                    super.onPageFinished(view, url)
                    val index = historyStack.indexOf(url)
                    if (index != -1) {
                        historyStack = historyStack.subList(0, index)
                    }
                    historyStack.add(url)
                }
            }
        }
    }

    // 拦截 back 键，若非新闻网首页退回新闻网首页，结束此 activity
    override fun onBackPressed() {
        if (historyStack.size == 1) {
            super.onBackPressed()
        } else {
            val map = HashMap<String, String>()
            map[headerKey] = headerToken
            historyStack.removeAt(historyStack.size - 1)
            val url = historyStack.last()
            webView.loadUrl(url, map)
        }
    }
}