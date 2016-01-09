package com.twt.service;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

import im.fir.sdk.FIR;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class WePeiYangApp extends Application{
    private static Context context;
    private static boolean isAppLunched;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePalApplication.initialize(this);
        FIR.init(this);
        super.onCreate();
    }


    public static Context getContext() {
        return context;
    }

    public static boolean isAppLunched() {
        return isAppLunched;
    }

    public static void setAppLunchState(Boolean argState) {
        isAppLunched = argState;
    }
}
