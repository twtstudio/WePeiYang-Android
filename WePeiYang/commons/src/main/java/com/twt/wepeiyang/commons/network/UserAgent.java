package com.twt.wepeiyang.commons.network;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.twt.wepeiyang.commons.experimental.CommonContext;

/**
 * Created by sunjuntao on 16/1/10.
 */
public class UserAgent {
    public static String getAppVersion() {
        PackageManager packageManager = CommonContext.INSTANCE.getApplication().getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(CommonContext.INSTANCE.getApplication().getPackageName(), 0);
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
        return Build.MODEL;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getCPU() {
        return Build.CPU_ABI;
    }

    public static String getDisplay() {
        return Build.DISPLAY;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String generate() {
        String ua = "WePeiYang/" + getAppVersion() + "(" + getBrand() + " " + getPhoneName() + ";" + "android " + getSystemVersion() + ";" + "cpu " + getCPU() + ";" + "rom " + getDisplay() + ")";
        return ua;
    }
}
