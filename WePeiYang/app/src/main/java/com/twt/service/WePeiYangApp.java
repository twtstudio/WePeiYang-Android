package com.twt.service;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.twt.service.router.RouterSchema;
import com.twt.service.router.WePeiYangRouter;
import com.twt.service.router.base.IWePeiYangRouteTableInitializer;
import com.twt.service.router.interceptors.AuthInterceptor;
import com.twt.service.ui.gpa.GpaActivity;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.main.MainActivity;

import org.litepal.LitePalApplication;

import java.util.Map;

import cn.campusapp.router.Router;
import im.fir.sdk.FIR;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class WePeiYangApp extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        LitePalApplication.initialize(this);
        initFir();
        initRouter();
    }

    private void initFir() {
        FIR.init(this);
        FIR.addCustomizeValue("sdk", android.os.Build.VERSION.SDK_INT + "");
        FIR.addCustomizeValue("cpu", android.os.Build.CPU_ABI);
        FIR.addCustomizeValue("rom_provider", android.os.Build.MANUFACTURER);
    }

    private void initRouter() {
        WePeiYangRouter router = new WePeiYangRouter(getApplicationContext(), new IWePeiYangRouteTableInitializer() {
            @Override
            public void initInterceptorTable(Map<String, String> interceptorTable) {
                interceptorTable.put(RouterSchema.GPA, AuthInterceptor.NAME);
            }

            @Override
            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
                router.put(RouterSchema.AUTH, LoginActivity.class);
                router.put(RouterSchema.HOME, MainActivity.class);
                router.put(RouterSchema.GPA, GpaActivity.class);
            }
        });
        router.addInterceptor(new AuthInterceptor());
        Router.addRouter(router);
        Router.setDebugMode(BuildConfig.LOG_ENABLE);
    }

    public static Context getContext() {
        return sContext;
    }
}
