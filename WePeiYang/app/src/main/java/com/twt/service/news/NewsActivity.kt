package com.twt.service.news

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.webkit.*
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.webView

/**
 * Created by tjwhm@TWTStudio at 8:06 PM, 2018/11/7.
 * Happy coding!
 */

class NewsActivity : AppCompatActivity() {

    // 天外天新闻网首页
    private val twtNewsHomeUrl = "https://news.twt.edu.cn/news"
    private lateinit var srl: SwipeRefreshLayout
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置沉浸式状态栏
        window.statusBarColor = getColorCompat(R.color.white)
        enableLightStatusBarMode(true)
        srl = swipeRefreshLayout {
            /*因为放了webview 是白色主题的，所以这里的设置会显得有点丑（？）*/
//            setColorSchemeColors(ContextCompat.getColor(this@NewsActivity, R.color.colorAccent))
            webView = webView {
                loadUrl(twtNewsHomeUrl)

                // 根据网页加载进度设置 swipeRefreshLayout isRefreshing
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        srl.isRefreshing = newProgress != 100
                    }
                }

                // 非天外天新闻网内容不予加载
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        return request?.url?.host?.equals("news.twt.edu.cn")?.not() ?: true
                    }
                }
            }

            onRefresh {
                webView.loadUrl(webView.url)
            }
        }
    }

    // 拦截 back 键，若非新闻网首页退回新闻网首页，若为新闻网首页结束此 activity
    override fun onBackPressed() {
        if (webView.url == twtNewsHomeUrl) {
            super.onBackPressed()
        } else {
            webView.loadUrl(twtNewsHomeUrl)
        }
    }
}
