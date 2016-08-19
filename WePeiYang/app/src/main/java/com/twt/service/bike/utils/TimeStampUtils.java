package com.twt.service.bike.utils;

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
}
