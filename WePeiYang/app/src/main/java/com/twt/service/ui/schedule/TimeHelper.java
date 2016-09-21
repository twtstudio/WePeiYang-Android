package com.twt.service.ui.schedule;

import com.twt.service.support.PrefUtils;

import java.util.Calendar;
import java.util.Locale;

import static com.amap.api.col.c.i;

/**
 * Created by tjliqy on 2016/9/18.
 */
public class TimeHelper {

    public static int getWeekInt(long startUnix){
        long i = System.currentTimeMillis()/1000  - startUnix;
        int day = (int) (i/86400);
        return day/7 + 1;
    }

    public static String getWeekString(int week){
        String sWeek;
        sWeek = week2Chinese(week);
        return sWeek;
    }

    private static String week2Chinese(int week){
        String[] cDay = {"零","一","二","三","四","五","六","七","八","九"};
        String sWeek = "";
        if (week/10 != 0){
            if (week/10 != 1){
                sWeek += cDay[week/10];
            }
            sWeek += "十";
        }
        sWeek += cDay[week%10];
        return sWeek;
    }
}