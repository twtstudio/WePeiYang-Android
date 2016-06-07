package com.twt.service.support;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.twt.service.WePeiYangApp;

/**
 * Created by sunjuntao on 16/1/10.
 */
public class UserAgent {
    // FIXME
    public static String getAppVersion() {
        PackageManager packageManager = WePeiYangApp.getContext().getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(WePeiYangApp.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = "";
        if (packInfo != null) {
            version = packInfo.versionName;
        }
        return version;
    }

    public static String getPhoneName() {
        return android.os.Build.MODEL;
    }

    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getCPU() {
        return android.os.Build.CPU_ABI;
    }

    public static String getDisplay() {
        return android.os.Build.DISPLAY;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String generate() {
        return  "WePeiYang/" + getAppVersion() + "(" + getBrand() + " " + getPhoneName() + ";" + "android " + getSystemVersion() + ";" + "cpu " + getCPU() + ";" + "rom " + getDisplay() + ")";
    }
}
