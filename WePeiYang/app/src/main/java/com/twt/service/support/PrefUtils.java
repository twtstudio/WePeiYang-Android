package com.twt.service.support;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.twt.service.WePeiYangApp;

/**
 * Created by Rex on 2015/8/3.
 */
public class PrefUtils {

    private static final String PREF_SHOW_DIALOG = "show_dialog";
    private static final String PREF_IS_LOGIN = "is_login";
    private static final String PREF_IS_KNOW_GPA_USAGE = "isknowgpausage";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_LOST_FOUND_CONTACT_NAME = "lost_found_contact_name";
    private static final String PREF_LOST_FOUND_CONTACT_NUMBER = "lost_found_contact_number";
    private static final String PREF_VERSION = "version";

    private static final String PREF_USER_REALNAME = "user_realname";
    private static final String PREF_USER_NUMBER = "user_number";
    private static final String PREF_SUBMIT_TITLE = "submit_title";
    private static final String PREF_SUBMIT_CONTENT = "submit_content";

    private static final String PREF_BIKE_TOKEN = "bike_token";
    private static final String PREF_BIKE_CARD_SIGN = "bike_sign";
    private static final String PREF_BIKE_CARD_ID = "bike_id";


    private static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(WePeiYangApp.getContext());
    }

    public static void setLogin(boolean isLogin) {
        getDefaultSharedPreferences().edit().putBoolean(PREF_IS_LOGIN, isLogin).apply();
    }

    public static boolean isLogin() {
        return getDefaultSharedPreferences().getBoolean(PREF_IS_LOGIN, false);
    }

    public static boolean isShowDiaLog() {
        return getDefaultSharedPreferences().getBoolean(PREF_SHOW_DIALOG, false);
    }
    public static void setShowDialog(boolean isFirstOpen) {
        getDefaultSharedPreferences().edit().putBoolean(PREF_SHOW_DIALOG, isFirstOpen).apply();
    }



    public static void setKnowGpaUsage(boolean isKnowGpaUsage) {
        getDefaultSharedPreferences().edit().putBoolean(PREF_IS_KNOW_GPA_USAGE, isKnowGpaUsage).apply();
    }

    public static boolean isKnowGpaUsage() {
        return getDefaultSharedPreferences().getBoolean(PREF_IS_KNOW_GPA_USAGE, false);
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

    public static String getTokenForBike(){
        return getDefaultSharedPreferences().getString(PREF_TOKEN,"");
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

    public static void setPrefVersion(String version){
        getDefaultSharedPreferences().edit().putString(PREF_VERSION, version).apply();
    }

    public static String getPreFversion(){
        return getDefaultSharedPreferences().getString(PREF_VERSION, "");
    }

    public static void setPrefUserRealName(String realName){
        getDefaultSharedPreferences().edit().putString(PREF_USER_REALNAME,realName).apply();
    }

    public static String getPrefUserRealname(){
        return getDefaultSharedPreferences().getString(PREF_USER_REALNAME,"");
    }

    public static void removePrefUserRealname(){
        getDefaultSharedPreferences().edit().remove(PREF_USER_REALNAME).commit();
    }

    public static void setPrefUserNumber(String userNumber){
        getDefaultSharedPreferences().edit().putString(PREF_USER_NUMBER, userNumber).apply();
    }

    public static void removePrefUserNumber(){
        getDefaultSharedPreferences().edit().remove(PREF_USER_NUMBER).commit();
    }

    public static String getPrefUserNumber(){
        return getDefaultSharedPreferences().getString(PREF_USER_NUMBER,"");
    }

    public static void setBikeToken(String token) {
        getDefaultSharedPreferences().edit().putString(PREF_BIKE_TOKEN, token).apply();
    }

    public static String getBikeToken() {
        return getDefaultSharedPreferences().getString(PREF_BIKE_TOKEN, "");
    }

    public static void setCardSign(String sign) {
        getDefaultSharedPreferences().edit().putString(PREF_BIKE_CARD_SIGN, sign).apply();
    }

    public static String getCardSign() {
        return getDefaultSharedPreferences().getString(PREF_BIKE_CARD_SIGN, "");
    }

    public static void setCardId(String id) {
        getDefaultSharedPreferences().edit().putString(PREF_BIKE_CARD_ID,id).apply();
    }

    public static String getCardId(){
        return getDefaultSharedPreferences().getString(PREF_BIKE_CARD_ID,"");
    }

    public static void setPrefSubmitTitle(String title){
        getDefaultSharedPreferences().edit().putString(PREF_SUBMIT_TITLE, title).apply();
    }

    public static void removePrefSubmitTitle(){
        getDefaultSharedPreferences().edit().remove(PREF_SUBMIT_TITLE).commit();
    }

    public static String getPrefSubmitTitle(){
        return getDefaultSharedPreferences().getString(PREF_SUBMIT_TITLE,"");
    }



    public static void setPrefSubmitContent(String submitContent){
        getDefaultSharedPreferences().edit().putString(PREF_SUBMIT_CONTENT, submitContent).apply();
    }

    public static void removePrefSubmitContent(){
        getDefaultSharedPreferences().edit().remove(PREF_SUBMIT_CONTENT).commit();
    }

    public static String getPrefSubmitContent(){
        return getDefaultSharedPreferences().getString(PREF_SUBMIT_CONTENT,"");
    }

}
