package com.bdpqchen.yellowpagesmodule.yellowpages.utils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by chen on 17-2-26.
 */

public class PrefUtils {


    private static final String IS_FIRST_OPEN = "is_first_open_then_init_the_database";
    private static final String YP_DATABASE_VERSION_CODE = "yp_database_version_code";

    public static boolean isFirstOpen(){
        return Hawk.get(IS_FIRST_OPEN, true);
    }
    public static void setIsFirstOpen(boolean b){
        Hawk.put(IS_FIRST_OPEN, b);
    }

    public static void setDatabaseVersion(int ypDatabaseVersionCode) {
        Hawk.put(YP_DATABASE_VERSION_CODE, ypDatabaseVersionCode);
    }
    public static int getDatabaseVersion(){
        return Hawk.get(YP_DATABASE_VERSION_CODE, 0);
    }

}
