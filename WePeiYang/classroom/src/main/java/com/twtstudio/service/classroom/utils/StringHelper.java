package com.twtstudio.service.classroom.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DefaultAccount on 2017/8/22.
 */

public class StringHelper {
    public static int getBuildingInt(String text){
        String regEx="楼\\D*\\d*\\D*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        System.out.println(m);
        int after=Integer.valueOf(m.replaceAll("").trim());
        return after;
    }
}
