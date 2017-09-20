package com.twtstudio.retrox.schedule.utils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by DefaultAccount on 2017/9/20.
 */

public class PrefUtil {
    private static final String IS_NEW_SCHEDULE="is_new_schedule";
    public static void setIsNewSchedule(boolean isNewSchedule){
        Hawk.put(IS_NEW_SCHEDULE,isNewSchedule);
    }
    public static boolean getIsNewSchedule() {
        return Hawk.get(IS_NEW_SCHEDULE,true);
    }
}
