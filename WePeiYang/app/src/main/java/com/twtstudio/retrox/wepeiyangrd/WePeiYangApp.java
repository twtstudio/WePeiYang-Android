package com.twtstudio.retrox.wepeiyangrd;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;


import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
 import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.wanjian.cockroach.Cockroach;


/**
 * Created by retrox on 2016/11/25.
 */

public class WePeiYangApp extends MultiDexApplication {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Bugly.init(getApplicationContext(), "8ceee186f2", false);
        CrashReport.setAppChannel(getApplicationContext(),"公测分发");
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);

//        Cockroach.install(new Cockroach.ExceptionHandler() {
//            @Override
//            public void handlerException(Thread thread, Throwable throwable) {
//                Logger.e(throwable,"crash");
//                CrashReport.postCatchedException(throwable,thread);
//            }
//        });

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
