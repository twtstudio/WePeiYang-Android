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
        var intent: Intent = getIntent()
        if (intent.hasExtra("recyclerViewUrl")) {
            var url: Int = intent.getIntExtra("recyclerViewUrl", 0)
            if (url != null) {
                webView = findViewById(R.id.webView)
                webView.loadUrl("https://news.twt.edu.cn/pernews/" + (url - 2))
            }
        } else if (intent.hasExtra("bannerUrl")) {
            var url: String = intent.getStringExtra("bannerUrl")
            if (url != null) {
                webView = findViewById(R.id.webView)
                webView.loadUrl("https://news.twt.edu.cn/pernews/" + url)
            }
        }


    }
}
