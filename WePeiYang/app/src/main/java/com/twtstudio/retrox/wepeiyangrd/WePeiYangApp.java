package com.twtstudio.retrox.wepeiyangrd;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.hawk.Hawk;

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
    }

    public static Context getContext() {
        return sContext;
    }
}
