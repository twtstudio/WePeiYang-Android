package com.twtstudio.retrox.wepeiyangrd.module.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twt.wepeiyang.commons.auth.login.LoginActivity;
import com.twtstudio.retrox.wepeiyangrd.base.BaseActivity;
import com.twtstudio.retrox.wepeiyangrd.home.HomeActivity;

/**
 * Created by retrox on 2017/1/20.
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isLogin = CommonPrefUtil.getIsLogin();
        if (isLogin){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
