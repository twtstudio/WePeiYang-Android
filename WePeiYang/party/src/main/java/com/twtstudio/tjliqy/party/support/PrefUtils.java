package com.twtstudio.tjliqy.party.support;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.twtstudio.tjliqy.party.WePeiYangAppOld;

/**
 * Created by Rex on 2015/8/3.
 */
public class PrefUtils {

    private static final String PREF_IS_LOGIN = "is_login";
    private static final String PREF_TOKEN = "token";;
    private static final String PREF_USERNAME = "username";
    private static final String PREF_USER_REALNAME = "user_realname";
    private static final String PREF_USER_NUMBER = "user_number";
    private static final String PREF_SUBMIT_TITLE = "submit_title";
    private static final String PREF_SUBMIT_CONTENT = "submit_content";
    private static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(WePeiYangAppOld.getContext());
    }

    public static void setLogin(boolean isLogin) {
        getDefaultSharedPreferences().edit().putBoolean(PREF_IS_LOGIN, isLogin).apply();
    }

    public static boolean isLogin() {
        return getDefaultSharedPreferences().getBoolean(PREF_IS_LOGIN, false);
    }

    public static void setToken(String token) {
        if (token != null) {
            getDefaultSharedPreferences().edit().putString(PREF_TOKEN, token ).apply();
        }
    }

    public static String getToken() {
        String token =  getDefaultSharedPreferences().getString(PREF_TOKEN, "");
        return "Bearer{" + token + "}";
    }

    public static void setUsername(String twtuname) {
        getDefaultSharedPreferences().edit().putString(PREF_USERNAME, twtuname).apply();
    }

    public static String getUsername() {
        return getDefaultSharedPreferences().getString(PREF_USERNAME, "未登录");
    }

    public static void removeUserName() {
        getDefaultSharedPreferences().edit().remove(PREF_USERNAME).apply();
    }

    public static void removeToken() {
        getDefaultSharedPreferences().edit().remove(PREF_TOKEN).apply();
    }

    public static void setPrefUserRealName(String realName){
        getDefaultSharedPreferences().edit().putString(PREF_USER_REALNAME,realName).apply();
    }

    public static String getPrefUserRealname(){
        return getDefaultSharedPreferences().getString(PREF_USER_REALNAME,"");
    }

    public static void removePrefUserRealname(){
        getDefaultSharedPreferences().edit().remove(PREF_USER_REALNAME).apply();
    }

    public static void setPrefUserNumber(String userNumber){
        getDefaultSharedPreferences().edit().putString(PREF_USER_NUMBER, userNumber).apply();
    }

    public static void removePrefUserNumber(){
        getDefaultSharedPreferences().edit().remove(PREF_USER_NUMBER).apply();
    }

    public static String getPrefUserNumber(){
        return getDefaultSharedPreferences().getString(PREF_USER_NUMBER,"");
    }

    public static void setPrefSubmitTitle(String title){
        getDefaultSharedPreferences().edit().putString(PREF_SUBMIT_TITLE, title).apply();
    }

    public static void removePrefSubmitTitle(){
        getDefaultSharedPreferences().edit().remove(PREF_SUBMIT_TITLE).apply();
    }

    public static String getPrefSubmitTitle(){
        return getDefaultSharedPreferences().getString(PREF_SUBMIT_TITLE,"");
    }

    public static void setPrefSubmitContent(String submitContent){
        getDefaultSharedPreferences().edit().putString(PREF_SUBMIT_CONTENT, submitContent).apply();
    }

    public static void removePrefSubmitContent(){
        getDefaultSharedPreferences().edit().remove(PREF_SUBMIT_CONTENT).apply();
    }

    public static String getPrefSubmitContent(){
        return getDefaultSharedPreferences().getString(PREF_SUBMIT_CONTENT,"");
    }
}
