package com.twtstudio.retrox.news.explore.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.launcher.ARouter;
import com.twtstudio.retrox.news.R;
import com.twtstudio.retrox.news.view.ProgressWebView;

/**
 * Created by retrox on 29/04/2017.
 */

public class VistaGalleryDetailActivity extends AppCompatActivity {

    private ProgressWebView progressWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_vista_detail_gallery);
        progressWebView = (ProgressWebView) findViewById(R.id.progress_webview);
        progressWebView.getSettings().setJavaScriptEnabled(true);
        progressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //第二个界面的点击看大图
                if (url.contains("work")) {
                    view.loadUrl("javascript:(function(){" +
                            "var objs = document.getElementsByTagName(\"img\"); " +
                            "for(var i=0;i<objs.length;i++)  " +
                            "{"
                            + "    objs[i].onclick=function()  " +
                            "    {  "
                            + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                            "    }  " +
                            "}" +
                            "})()");
                }
                super.onPageFinished(view, url);
            }

        });
        progressWebView.getSettings().setUserAgentString("wepeiyang");
        progressWebView.addJavascriptInterface(new MJavascriptInterface(), "imagelistener");

        String url = "https://photograph.twtstudio.com/list/1";

        progressWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && progressWebView.canGoBack()) {
            progressWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    static class MJavascriptInterface {

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            ARouter.getInstance().build("/photo/preview").withString("url", img).navigation();
        }
    }

}
