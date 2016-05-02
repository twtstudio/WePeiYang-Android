package com.twt.service.ui.date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.twt.service.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DatingActivity extends AppCompatActivity {

    private static final String URL = "http://yueba.twtstudio.com";
    @InjectView(R.id.wv_dating)
    WebView wvDating;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DatingActivity.class);
        context.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating);
        ButterKnife.inject(this);
        wvDating.getSettings().setJavaScriptEnabled(true);
        wvDating.loadUrl(URL);
    }
}
