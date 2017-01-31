package com.twt.wepeiyang.commons.utils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by retrox on 2016/12/12.
 * A Secure Preference Control
 * 用于通用的数据存放，比如说TOKEN，其他的独立数据在自己的module里面写
 */

public class CommonPrefUtil {

    private static final String IS_LOGIN = "is_login";
    private static final String IS_KNOW_GPA_USAGE = "isknowgpausage";
    private static final String TOKEN = "token";
    private static final String USERNAME = "username";
    private static final String LOST_FOUND_CONTACT_NAME = "lost_found_contact_name";
    private static final String LOST_FOUND_CONTACT_NUMBER = "lost_found_contact_number";
    private static final String VERSION = "version";
    private static final String BIKE_TOKEN = "bike_token";
    private static final String BIKE_CARD_SIGN = "bike_sign";
    private static final String BIKE_CARD_ID = "bike_id";

    private static final String THEME_MODE = "theme"; //false is normal , true is night

    public static void clearAll(){
        Hawk.deleteAll();
    }

    public static void setToken(String token){
        if (null != token) {
            Hawk.put(TOKEN,token);
        }
    }

    public static String getToken(){
        return Hawk.get(TOKEN," ");
    }

    public static void setIsLogin(boolean isLogin){
        Hawk.put(IS_LOGIN,isLogin);
    }

    public static boolean getIsLogin(){
        return Hawk.get(IS_LOGIN,false);
    }

    public static void setThemeMode(boolean themeMode){
        Hawk.put(THEME_MODE,themeMode);
    }

    public static boolean getThemeMode(){
        return Hawk.get(THEME_MODE,false);
    }
}
