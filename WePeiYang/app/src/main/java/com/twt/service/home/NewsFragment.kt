package com.twt.service.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.twt.service.R
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.webView


class NewsFragment: Fragment() {
    private val twtNewsHomeUrl = "https://news.twt.edu.cn/news"
    private lateinit var srl: SwipeRefreshLayout
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view: View = inflater.inflate(R.layout.activity_home_new, container, false)
        val mContext = activity
//        val itemManager = ItemManager()
//        val imageView = view.findViewById<ImageView>(R.id.iv_toolbar_avatar).apply {
//            setOnClickListener {
//                mContext?.startActivity<FragmentActivity>("frag" to "User")
//            }
//        }

        srl = mContext?.swipeRefreshLayout {
            /*因为放了webview 是白色主题的，所以这里的设置会显得有点丑（？）*/
//            setColorSchemeColors(ContextCompat.getColor(this@NewsActivity, R.color.colorAccent))
            webView = webView {

                //微北洋新闻页面有问题的原因是 之前没有允许js执行
                settings.javaScriptEnabled = true;

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
        }!!
        return view
    }
}