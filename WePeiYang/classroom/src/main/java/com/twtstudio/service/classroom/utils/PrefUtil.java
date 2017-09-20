package com.twtstudio.service.classroom.utils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by DefaultAccount on 2017/9/20.
 */

public class PrefUtil {
    private static final String IS_NEW_CAMPUS="is_new_campus";
    private static final String HAS_CHOSEN_CAMPUS="has_chosen_campus";
    public static void setIsNewCampus(boolean isNewCampus){
        Hawk.put(IS_NEW_CAMPUS,isNewCampus);
    }

    public static boolean getIsNewCampus() {
        return Hawk.get(IS_NEW_CAMPUS,true);
    }
    public static void setHasChosenCampus(boolean hasChosenCampus) {
        Hawk.put(HAS_CHOSEN_CAMPUS,hasChosenCampus);
    }

    public static boolean getHasChosenCampus() {
        return  Hawk.get(HAS_CHOSEN_CAMPUS,false);
    }
}
