package com.twt.service.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.twt.service.R;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.main.MainActivity;

/**
 * Created by Rex on 2015/8/3.
 */
public class WelcomeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (PrefUtils.isLogin()) {
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
