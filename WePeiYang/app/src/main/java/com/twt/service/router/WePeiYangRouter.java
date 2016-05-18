package com.twt.service.router;

import android.content.Context;
import android.text.TextUtils;

import com.twt.service.router.base.IWePeiYangRouteTableInitializer;
import com.twt.service.router.base.RouterInterceptor;

import java.util.HashMap;
import java.util.Map;

import cn.campusapp.router.router.ActivityRouter;

/**
 * Created by huangyong on 16/5/18.
 */
public class WePeiYangRouter extends ActivityRouter {

    protected Map<String, RouterInterceptor> mInterceptorContainer = new HashMap<>();
    protected Map<String, String> mInterceptorTable = new HashMap<>();

    public WePeiYangRouter(Context appContext, IWePeiYangRouteTableInitializer initializer) {
        init(appContext, initializer);
    }

    public void init(Context appContext, IWePeiYangRouteTableInitializer initializer) {
        super.init(appContext, initializer);
        initInterceptorTable(initializer);
    }

    protected void initInterceptorTable(IWePeiYangRouteTableInitializer initializer) {
        if (initializer != null) {
            initializer.initInterceptorTable(mInterceptorTable);
        }
    }

    public void addInterceptor(RouterInterceptor interceptor) {
        if (interceptor != null) {
            if (!TextUtils.isEmpty(interceptor.getName())) {
                mInterceptorContainer.put(interceptor.getName(), interceptor);
            }
        }
    }

    @Override
    public boolean canOpenTheUrl(String url) {
        boolean isMatch = super.canOpenTheUrl(url);
        if (isMatch) {
            if (!mInterceptorTable.containsKey(url)) {
                return true;
            }

            String key = mInterceptorTable.get(url);
            return !mInterceptorContainer.containsKey(key) || mInterceptorContainer.get(key).intercept(url);

        }
        return false;
    }
}
