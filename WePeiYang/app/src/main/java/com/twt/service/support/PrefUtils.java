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
    private static final String PREF_TOKEN = "token";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_LOST_FOUND_CONTACT_NAME = "lost_found_contact_name";
    private static final String PREF_LOST_FOUND_CONTACT_NUMBER = "lost_found_contact_number";
    private static final String PREF_IS_GUIDE = "is_guide";


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
        if (token != null) {
            getDefaultSharedPreferences().edit().putString(PREF_TOKEN, "Bearer{" + token + "}").apply();
        }
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

    public static void removeUserName() {
        getDefaultSharedPreferences().edit().remove(PREF_USERNAME).commit();
    }

    public static void removeToken() {
        getDefaultSharedPreferences().edit().remove(PREF_TOKEN).commit();
    }

    public static void setLostFoundContactName(String name) {
        getDefaultSharedPreferences().edit().putString(PREF_LOST_FOUND_CONTACT_NAME, name).apply();
    }

    public static String getLostFoundContactName() {
        return getDefaultSharedPreferences().getString(PREF_LOST_FOUND_CONTACT_NAME, "");
    }

    public static void setLostFoundContactNumber(String number) {
        getDefaultSharedPreferences().edit().putString(PREF_LOST_FOUND_CONTACT_NUMBER, number).apply();
    }

    public static String getLostFoundContactNumber() {
        return getDefaultSharedPreferences().getString(PREF_LOST_FOUND_CONTACT_NUMBER, "");
    }

    public static void setPrefIsGuide(boolean isGuide){
        getDefaultSharedPreferences().edit().putBoolean(PREF_IS_GUIDE, isGuide).apply();
    }

    public static boolean isGuide(){
        return getDefaultSharedPreferences().getBoolean(PREF_IS_GUIDE, true);
    }
}
