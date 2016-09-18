package com.twt.service.ui.schedule;

import com.twt.service.support.PrefUtils;

import java.util.Calendar;

/**
 * Created by tjliqy on 2016/9/18.
 */
public class TimeHelper {

    public static void setStartWeekDate(int updateWeek, Calendar updateDay){
        updateDay.add(Calendar.WEEK_OF_MONTH, -updateWeek);
    }
}
