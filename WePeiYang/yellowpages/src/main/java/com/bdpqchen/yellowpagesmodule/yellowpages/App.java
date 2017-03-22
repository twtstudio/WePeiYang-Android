package com.bdpqchen.yellowpagesmodule.yellowpages;

import android.app.Application;
import android.content.Context;

import com.bdpqchen.yellowpagesmodule.yellowpages.database.GreenDaoManager;
import com.inst.greendao3_demo.dao.DaoSession;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by chen on 17-2-21.
 */

public class App extends Application {


    public static Context getContext() {
        return com.twt.wepeiyang.commons.utils.App.getApplicationContext();
    }


    private void initDependencies() {
//        Logger.init("logger").hideThreadInfo().logLevel(LogLevel.FULL);
//        Hawk.init(mContext).build();
        GreenDaoManager.getInstance();
    }

}
