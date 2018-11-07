package com.example.news2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView


class WebActivity : AppCompatActivity() {
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val intent: Intent = intent
        if (intent.hasExtra("recyclerViewUrl")) {
            val url: Int = intent.getIntExtra("recyclerViewUrl", 0)
            webView = findViewById(R.id.news2_web_view)

            // 这个 API 是错误的，列表 JSON 中的新闻 index 拼在新闻详情的 URL 后面是错误的
            // 而且并没有什么规律，等待后台修这个 BUG（-2 是试出来的有新闻的比较多的一种算法，哭泣）
            // ps 之前这里错怪了森哥
            webView.loadUrl("https://news.twt.edu.cn/pernews/${url - 2}")
        } else if (intent.hasExtra("bannerUrl")) {
            val url: String = intent.getStringExtra("bannerUrl")
            webView = findViewById(R.id.news2_web_view)
            webView.loadUrl("https://news.twt.edu.cn/pernews/$url")
        }


    }
}
