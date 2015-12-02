package com.rex.wepeiyang.support;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rex.wepeiyang.WePeiYangApp;

/**
 * Created by Rex on 2015/8/3.
 */
public class PrefUtils {

    private static final String PREF_IS_LOGIN = "is_login";
    private static final String PREF_TJU_UNAME = "tjuuname";
    private static final String PREF_TJU_PASSWD = "tjupasswd";
    private static final String PREF_IS_KNOW_GPA_USAGE = "isknowgpausage";
    private static final String PREF_CAPTCHA_TOKEN = "captchatoken";


    private static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(WePeiYangApp.getContext());
    }

    public static void setLogin(boolean isLogin) {
        getDefaultSharedPreferences().edit().putBoolean(PREF_IS_LOGIN, isLogin).apply();
    }

    public static boolean isLogin() {
        return getDefaultSharedPreferences().getBoolean(PREF_IS_LOGIN, false);
    }

    public static void setTjuuname(String tjuuname) {
        getDefaultSharedPreferences().edit().putString(PREF_TJU_UNAME, tjuuname).apply();
    }

    public static String getTjuUname() {
        return getDefaultSharedPreferences().getString(PREF_TJU_UNAME, null);
    }

    public static void setTjuPasswd(String tjupasswd) {
        getDefaultSharedPreferences().edit().putString(PREF_TJU_PASSWD, tjupasswd).apply();
    }

    public static String getTjuPasswd() {
        return getDefaultSharedPreferences().getString(PREF_TJU_PASSWD, null);
    }

    public static void setKnowGpaUsage(boolean isKnowGpaUsage) {
        getDefaultSharedPreferences().edit().putBoolean(PREF_IS_KNOW_GPA_USAGE, isKnowGpaUsage).apply();
    }

    public static boolean isKnowGpaUsage() {
        return getDefaultSharedPreferences().getBoolean(PREF_IS_KNOW_GPA_USAGE, false);
    }
}
