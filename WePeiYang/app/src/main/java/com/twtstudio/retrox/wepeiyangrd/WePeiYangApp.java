package com.twtstudio.retrox.wepeiyangrd;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
 import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * Created by retrox on 2016/11/25.
 */

public class WePeiYangApp extends MultiDexApplication {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "8ceee186f2", false);
        CrashReport.setAppChannel(getApplicationContext(),"内测分发");
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);
        sContext = getApplicationContext();
        Hawk.init(sContext).build();
        Fresco.initialize(sContext);
//        CrashReport.initCrashReport(getApplicationContext(), "8ceee186f2", true);


//        Stetho.initializeWithDefaults(getApplicationContext());
    }



    public static Context getContext() {
        return sContext;
    }
}
