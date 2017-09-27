package com.twt.service.network;

import android.app.Application;
import android.content.Context;
import android.support.compat.BuildConfig;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by chen on 2017/7/18.
 */

public class NetApp extends Application {
    private static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();

        CrashReport.setAppChannel(getApplicationContext(),"公测分发");
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);

        sContext = getApplicationContext();
        Hawk.init(sContext).build();
        Fresco.initialize(sContext);
        BigImageViewer.initialize(GlideImageLoader.with(this));

        if (BuildConfig.DEBUG){
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }
    public static Context getContext() {
        return sContext;
    }
}
