package com.twt.wepeiyang.commons;

/**
 * Created by sunjuntao on 16/1/7.
 * gg
 */
public class JniUtils {
//    static {
//        System.loadLibrary("Jni");
//    }

    public  String getAppKey(){
        return "9GTdynvrCm1EKKFfVmTC";
    }

    public String getAppSecret(){
       return "1aVhfAYBFUfqrdlcT621d9d6OzahMI";
    }

    public String getFirApiToken(){
        return "421d45c1fa7b7c4358667ffcedf4638e";
    }

    private JniUtils() {
    }

    public static JniUtils getInstance() {
        return JniUtilsCarry.instance;
    }

    private static class JniUtilsCarry {
        private static final JniUtils instance = new JniUtils();
    }
}
