package com.twtstudio.service.classroom.utils;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import java.util.Calendar;

/**
 * Created by zhangyulong on 7/13/17.
 */

public class TimeHelper {
    public static int getWeekInt(){
        Calendar calendar=Calendar.getInstance();
        long i = calendar.getTimeInMillis()/1000  - CommonPrefUtil.getStartUnix();
        int day = (int) (i/86400);
        if(day<0) return -1;
        int week =  day/7 + 1;
        return week;

    }
    public static int getTimeInt(){
        Calendar calendar=Calendar.getInstance();
        int hours=calendar.getTime().getHours();
//        switch (calendar.getTime().getHours()){
//            case 8:
//                break;
//            case 9:
//                break;
//            case 10:
//                break;
//            case 11:
//                break;
//            case 13:
//                break;
//            case 14:
//                break;
//            case 15:
//                break;
//            case 16:
//                break;
//            case 17:
//                break;
//            case 18:
//                break;
//            case 19:
//                break;
//            case 20:
//                break;
//            case 21:
//                break;
//        }
        if(hours<12&&hours>=8) return hours-7;
        if(hours>=13&&hours<17) return hours-12+4;
        if(hours>=18&&hours<23) return hours-17+8;
        return -1;
    }
    public static int getDayOfWeek(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK)-1;
    }
}
