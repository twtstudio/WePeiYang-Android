package com.twt.service.router.interceptors;

import android.text.TextUtils;

import com.twt.service.router.base.RouterInterceptor;
import com.twt.service.support.PrefUtils;

/**
 * Created by huangyong on 16/5/18.
 */
public class AuthInterceptor implements RouterInterceptor {

    public static final String NAME = "auth";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean intercept(String url) {
        String token = PrefUtils.getToken();
        return !TextUtils.isEmpty(token);
    }

}
