package com.twtstudio.retrox.wepeiyangrd;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * Created by retrox on 2016/11/25.
 */

public class WePeiYangApp extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Hawk.init(sContext).build();
        Fresco.initialize(sContext);
//        CrashReport.initCrashReport(getApplicationContext(), "8ceee186f2", true);

        Bugly.init(getApplicationContext(), "8ceee186f2", false);
        CrashReport.setAppChannel(getApplicationContext(),"内测分发");
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);

    }

    public static Context getContext() {
        return sContext;
    }
}
