package com.twtstudio.retrox.bike.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.twt.wepeiyang.commons.experimental.Commons;
import com.twt.wepeiyang.commons.utils.CommonPreferences;

/**
 * Created by Rex on 2015/8/3.
 */
public class PrefUtils {

    private static final String PREF_SHOW_DIALOG = "show_dialog";
    private static final String PREF_IS_LOGIN = "is_login";
    private static final String PREF_IS_KNOW_GPA_USAGE = "isknowgpausage";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_GPA_TOKEN = "gpatoken";
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
    private static final String PREF_BIKE_IS_BIND = "bike_is_bind";

    private static final String PREF_READ_TOKEN = "read_token";

    private static final String PREF_SCHEDULE_START_DATE = "schedule_start_date";

    private static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(Commons.INSTANCE.getApplicationContext());
    }

    /**
     * 已经迁移到新版到公共preference
     *
     * @return
     */
    public static String getToken() {
//        String token =  getDefaultSharedPreferences().getString(PREF_TOKEN, "");
        String token = CommonPreferences.INSTANCE.getToken();
        return "Bearer{" + token + "}";
    }

    public static void setToken(String token) {
        if (token != null) {
            getDefaultSharedPreferences().edit().putString(PREF_TOKEN, token).apply();
        }
    }

    /**
     * 已经迁移到新版到公共preference
     *
     * @return
     */
    public static String getTokenForBike() {
        return CommonPreferences.INSTANCE.getToken();
    }

    public static String getBikeToken() {
        return getDefaultSharedPreferences().getString(PREF_BIKE_TOKEN, "nothing");
    }

    public static void setBikeToken(String token) {
        getDefaultSharedPreferences().edit().putString(PREF_BIKE_TOKEN, token).apply();
//        CommonPreferences.setIsBindBike(true);
    }

    public static String getCardSign() {
        return getDefaultSharedPreferences().getString(PREF_BIKE_CARD_SIGN, "");
    }

    public static void setCardSign(String sign) {
        getDefaultSharedPreferences().edit().putString(PREF_BIKE_CARD_SIGN, sign).apply();
    }

    public static String getCardId() {
        return getDefaultSharedPreferences().getString(PREF_BIKE_CARD_ID, "");
    }

    public static void setCardId(String id) {
        getDefaultSharedPreferences().edit().putString(PREF_BIKE_CARD_ID, id).apply();
    }

    public static void setBikeIsBindState(boolean isBind) {
        getDefaultSharedPreferences().edit().putBoolean(PREF_BIKE_IS_BIND, isBind).apply();
    }


    public static String getReadToken() {
        return getDefaultSharedPreferences().getString(PREF_READ_TOKEN, "");
    }

    public static void setReadToken(String readToken) {
        getDefaultSharedPreferences().edit().putString(PREF_READ_TOKEN, "Bearer {" + readToken + "}").apply();
    }

    public static void removeReadToken() {
        getDefaultSharedPreferences().edit().remove(PREF_READ_TOKEN).apply();
    }
}
