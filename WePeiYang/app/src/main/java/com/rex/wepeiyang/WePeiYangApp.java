package com.rex.wepeiyang;

import android.app.Application;
import android.content.Context;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class WePeiYangApp extends Application{
    private static Context context;
    private static boolean isAppLunched;

    @Override
    public void onCreate() {
        context = getApplicationContext();
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
