package com.twt.service.ui.bus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.twt.service.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BusActivity extends AppCompatActivity {

    @InjectView(R.id.wv_bus)
    WebView wvBus;

    private static final String URL = "http://bus.twtstudio.com/tp_bus/index.php/Home/Index/dl";

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BusActivity.class);
        context.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        ButterKnife.inject(this);
        wvBus.getSettings().setJavaScriptEnabled(true);
        wvBus.loadUrl(URL);
    }
}
