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
        int minutes=calendar.getTime().getMinutes();
        switch (hours) {
            case 8:
                if(minutes>=30) return 1;
                break;
            case 9:
                if(minutes<=15) return 1;
                else  return 2;
            case 10:
                if(minutes<=5) return 2;
                else  return 3;
            case 11:
                if(minutes<=10) return 3;
                else return 4;
            case 13:
                if(minutes>=30) return 5;
                break;
            case 14:
                if(minutes<=15) return 5;
                else  return 6;
            case 15:
                if(minutes<=5) return 6;
                else  return 7;
            case 16:
                if(minutes<=10) return 7;
                else  return 8;
            case 18:
                if(minutes>=30) return 9;
                break;
            case 19:
                if(minutes<=15) return 9;
                else  return 10;
            case 20:
                if(minutes<=5) return 10;
                else if(minutes<=55) return 11;
                break;
            case 21:
                if(minutes<=45) return 12;
                break;
        }
        return 20;

//        if(hours<12&&hours>=8) return hours-7;
//        if(hours>=13&&hours<17) return hours-12+4;
//        if(hours>=18&&hours<22) return hours-17+8;
//        return 20;
    }
    public static int getDayOfWeek(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK)-1;
    }

}
