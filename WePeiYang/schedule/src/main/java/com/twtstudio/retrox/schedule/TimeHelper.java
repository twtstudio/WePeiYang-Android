package com.twtstudio.retrox.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tjliqy on 2016/9/18.
 */
public class TimeHelper {

    //test
    public static int getWeekInt(long startUnix, Calendar calendar){
        long i = calendar.getTimeInMillis()/1000  - startUnix;
        int day = (int) (i/86400);
        if(day<0) return 1;
        int week =  day/7 + 1;
        //有人过了那个周数就想看下学期hhh
        if (week > 20){
            return -1;
        }else {
            return week;
        }
    }
    public static int getRealWeekInt(long startUnix,Calendar calendar){
        long i = calendar.getTimeInMillis()/1000  - startUnix;
        int day = (int) (i/86400);
        int week =  day/7 + 1;
        return week;
    }

    public static String getWeekString(int week){
        String[] cDay = {"零","一","二","三","四","五","六","七","八","九"};
        String sWeek = "";
        if (week/10 != 0 || week == 0){
            if (week/10 != 1){
                sWeek += cDay[week/10];
            }
            if (week != 0){
                sWeek += "十";
            }
        }
        if (week%10 != 0) {
            sWeek += cDay[week % 10];
        }
        return sWeek;
    }

    public static String getChineseCharacter(int num){
        String[] cDay = {"零","一","二","三","四","五","六","日"};
        return cDay[num];

    }

    public static void getWeekDate(long startUnix, int week){
        startUnix += week*7*86400*1000;
        Date date = new Date(startUnix);
        SimpleDateFormat eDateFormat = new SimpleDateFormat("E");
        int iE =Integer.parseInt(eDateFormat.format(date));
    }
}