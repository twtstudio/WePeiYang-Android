package com.twt.service;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


/**
 * Created by retrox on 2016/11/25.
 */

public class WePeiYangApp extends MultiDexApplication {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Bugly.init(getApplicationContext(), "8ceee186f2", false);
        CrashReport.setAppChannel(getApplicationContext(), "公测分发");
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
        UMShareAPI.get(this);
        BigImageViewer.initialize(GlideImageLoader.with(this));

        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);


    }


    {
        //友盟社会化分享的KEY（目前只有QQ的）
        Config.DEBUG = true;

        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("1104743406", "Hbj7yk7xd1bgrMPG");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }
    public static Context getContext() {
        return sContext;
    }
}
