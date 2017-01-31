package com.twt.service.rxsrc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jcy on 2016/8/13.
 */

public class TimeStampUtils {
    public static String getDateString(String timeStamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long timeSta = Long.valueOf(timeStamp);
        return dateFormat.format(new Date(timeSta*1000L));
    }
    public static String getSimpleDateString(String timeStamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
        long timeSta = Long.valueOf(timeStamp);
        return dateFormat.format(new Date(timeSta*1000L));
    }
    public static String getSimpleMonthString(String curmills){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        long timeSta = Long.valueOf(curmills);
        return dateFormat.format(timeSta);
    }
}
