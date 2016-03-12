package com.twt.service;

/**
 * Created by sunjuntao on 16/1/7.
 */
public class JniUtils {
    static {
        System.loadLibrary("Jni");
    }

    public native String getAppKey();

    public native String getAppSecret();

    public native String getFirApiToken();

    private JniUtils() {

    }

    public static JniUtils getInstance() {
        return JniUtilsCarry.instance;
    }

    private static class JniUtilsCarry {
        private static final JniUtils instance = new JniUtils();
    }
}
