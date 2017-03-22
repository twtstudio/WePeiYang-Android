package com.bdpqchen.yellowpagesmodule.yellowpages.utils;

/**
 * Created by bdpqchen on 17-3-3.
 */

public class TextFormatUtils {

    public  static String getPhoneNum(String str) {
        String dest = "";
        if (str != null) {
            dest = str.replaceAll("[^0-9]","");

        }
        return dest;
    }
}
