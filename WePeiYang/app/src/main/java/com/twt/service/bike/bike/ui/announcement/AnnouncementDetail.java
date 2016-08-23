package com.twt.service.bike.bike.ui.announcement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.twt.service.R;
import com.twt.service.bike.common.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jcy on 2016/8/23.
 */

public class AnnouncementDetail extends BaseActivity {
    @InjectView(R.id.announcement_web)
    WebView mWebView;
    @InjectView(R.id.announcement_toolbar)
    Toolbar mToolBar;

    @Override
    protected int getLayout() {
        return R.layout.activity_bike_announcement_detail;
    }

    @Override
    protected void actionStart(Context context) {

    }

    @Override
    protected int getStatusbarColor() {
        return R.color.bike_toolbar_color;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Toolbar getToolbar() {
        return mToolBar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        Intent intent = getIntent();
        String s = intent.getStringExtra("detail");
        String title = intent.getStringExtra("title");
        setTitle(title);
        mWebView.loadData(s,"text/html;charset=utf-8",null);
    }
}
