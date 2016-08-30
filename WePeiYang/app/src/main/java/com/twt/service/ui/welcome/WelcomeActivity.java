package com.twt.service.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.github.mikephil.charting.utils.Utils;
import com.twt.service.R;
import com.twt.service.support.ACache;
import com.twt.service.support.ApplicationUtils;
import com.twt.service.support.CacheLogoTask;
import com.twt.service.support.PrefUtils;
import com.twt.service.support.UserAgent;
import com.twt.service.ui.guide.GuideActivity;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.main.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Rex on 2015/8/3.
 */
public class WelcomeActivity extends Activity {
//    @InjectView(R.id.tv_version_name)
//    TextView tvVersionName;
    private static boolean hasCacheLogo = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);
//        tvVersionName.setText(UserAgent.getAppVersion());
        if (!hasCacheLogo) {
            CacheLogoTask cacheLogoTask = new CacheLogoTask(this);
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.execute(cacheLogoTask);
            executor.shutdown();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
//                if(!PrefUtils.getPreFversion().equals(ApplicationUtils.getVersionName())){
//                    intent = new Intent(WelcomeActivity.this, GuideActivity.class);
//                }else
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
