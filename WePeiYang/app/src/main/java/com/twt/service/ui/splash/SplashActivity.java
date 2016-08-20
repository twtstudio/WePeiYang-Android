package com.twt.service.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.twt.service.ui.welcome.WelcomeActivity;

/**
 * Created by tjliqy on 2016/8/20.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }
}
