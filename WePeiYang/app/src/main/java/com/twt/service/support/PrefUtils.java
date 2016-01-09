package com.twt.service.support;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.twt.service.WePeiYangApp;

/**
 * Created by Rex on 2015/8/3.
 */
public class PrefUtils {

    private static final String PREF_IS_LOGIN = "is_login";
    private static final String PREF_IS_KNOW_GPA_USAGE = "isknowgpausage";
    private static final String PREF_CAPTCHA_TOKEN = "captchatoken";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_USERNAME = "username";


    private static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(WePeiYangApp.getContext());
    }

    public static void setLogin(boolean isLogin) {
        getDefaultSharedPreferences().edit().putBoolean(PREF_IS_LOGIN, isLogin).apply();
    }

    public static boolean isLogin() {
        return getDefaultSharedPreferences().getBoolean(PREF_IS_LOGIN, false);
    }

    public static void setKnowGpaUsage(boolean isKnowGpaUsage) {
        getDefaultSharedPreferences().edit().putBoolean(PREF_IS_KNOW_GPA_USAGE, isKnowGpaUsage).apply();
    }

    public static boolean isKnowGpaUsage() {
        return getDefaultSharedPreferences().getBoolean(PREF_IS_KNOW_GPA_USAGE, false);
    }

    public static void setToken(String token) {
        getDefaultSharedPreferences().edit().putString(PREF_TOKEN, token).apply();
    }

    public static String getToken() {
        return getDefaultSharedPreferences().getString(PREF_TOKEN, "");
    }

    public static void setUsername(String twtuname) {
        getDefaultSharedPreferences().edit().putString(PREF_USERNAME, twtuname).apply();
    }

    public static String getUsername() {
        return getDefaultSharedPreferences().getString(PREF_USERNAME, "未登录");
    }
}
