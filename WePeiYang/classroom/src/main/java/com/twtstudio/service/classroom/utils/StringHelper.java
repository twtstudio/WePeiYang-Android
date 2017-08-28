package com.twtstudio.service.classroom.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DefaultAccount on 2017/8/22.
 */

public class StringHelper {
    public static int getBuildingInt(String text) {
        int after= 0;
        try {
            String regEx="楼.*";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(text);
            System.out.println(m);
            after = Integer.valueOf(m.replaceAll("").trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return after;
    }
}
