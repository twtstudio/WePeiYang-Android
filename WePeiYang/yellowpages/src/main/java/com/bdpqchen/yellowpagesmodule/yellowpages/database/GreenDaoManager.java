package com.bdpqchen.yellowpagesmodule.yellowpages.database;

import android.content.Context;

import com.bdpqchen.yellowpagesmodule.yellowpages.App;
import com.inst.greendao3_demo.dao.DaoMaster;
import com.inst.greendao3_demo.dao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by chen on 17-2-26.
 */
public class GreenDaoManager {
    public static String DB_NAME = "yellow_pages.db";
    
    private static GreenDaoManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private DaoMaster.DevOpenHelper devOpenHelper;

    public GreenDaoManager() {
        //创建一个数据库
//        devOpenHelper = new DaoMaster.DevOpenHelper(App.getContext(), DB_NAME, null);
//        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
//        mDaoSession = mDaoMaster.newSession();

    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }
        return mInstance;
    }

    public DaoMaster getDaoMaster() {
        if (null == mDaoMaster){
            devOpenHelper = new DaoMaster.DevOpenHelper(App.getContext(), DB_NAME, null);
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    public DaoSession getDaoSession(){
        if(null == mDaoSession){
            if (null == mDaoMaster){
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    //debug mode, default=false
    public void setDebug(boolean flag){
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    /**
     * 关闭数据库连接及Session
     */
    public void close() {

        if (mDaoSession != null){
            mDaoSession.clear();
            mDaoSession = null;
        }

        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }
}