package com.twt.service.support;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.twt.service.WePeiYangApp;


public class ApplicationUtils {

    private static final String DEFAULT_ERROR_STRING = "0.0.0";
    private static final int DEFAULT_ERROR_INTEGER = -1;
    private static final Context context = WePeiYangApp.getContext();

    public static String getVersionName() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return DEFAULT_ERROR_STRING;
        }
    }

    public static int getVersionCode() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            return DEFAULT_ERROR_INTEGER;
        }
    }

}
