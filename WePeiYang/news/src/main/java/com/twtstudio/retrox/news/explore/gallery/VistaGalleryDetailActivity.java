package com.twtstudio.retrox.news.explore.gallery;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.twtstudio.retrox.news.R;
import com.twtstudio.retrox.news.view.ProgressWebView;

/**
 * Created by retrox on 29/04/2017.
 */

public class VistaGalleryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_vista_detail_gallery);
        ProgressWebView progressWebView = (ProgressWebView) findViewById(R.id.progress_webview);
        progressWebView.getSettings().setJavaScriptEnabled(true);
        progressWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        String url = "https://photograph.twtstudio.com/list/1";
        progressWebView.loadUrl(url);
    }
}
