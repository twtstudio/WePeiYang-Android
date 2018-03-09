package com.twtstudio.retrox.tjulibrary.homeitem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by retrox on 2017/2/21.
 */

public class BookTimeHelper {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    /**
     * 计算时间间隔 "yyyy-MM-dd"
     *
     * @param t1
     * @param t2
     * @return t1-t2 (day)
     */
    public static int getBetweenDays(String t1, String t2) {
        long between = 0;
        try {
            between = dateFormat.parse(t1).getTime() - dateFormat.parse(t2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = (int) (between / (24 * 60 * 60 * 1000));
        return day;
    }

    /**
     * 计算剩余时间
     *
     * @param t1
     * @return
     */
    public static int getBetweenDays(String t1) {
        long between = 0;
        try {
            between = dateFormat.parse(t1).getTime() - System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = (int) (between / (24 * 60 * 60 * 1000));
        return day;
    }
}
