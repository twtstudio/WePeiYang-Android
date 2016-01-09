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
}
