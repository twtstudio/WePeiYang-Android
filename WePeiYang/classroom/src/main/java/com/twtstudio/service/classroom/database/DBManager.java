package com.twtstudio.service.classroom.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.model.FreeRoom2;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by DefaultAccount on 2017/8/21.
 */

public class DBManager {
    private final static String dbName = "room_collection_db";
    private static DBManager mInstance;
    private  DaoMaster.DevOpenHelper openHelper;
    private static Context mContext;

    public DBManager() {
        openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
    }

    private static class DBManagerHolder{
        private static final DBManager INSTANCE=new DBManager();
    }
    public static final DBManager getInstance(Context context){
        mContext=context;
        return DBManagerHolder.INSTANCE;
    }
//    public static DBManager getInstance(Context context) {
//        if (mInstance == null) {
//            synchronized (DBManager.class) {
//                if (mInstance == null) {
//                    mInstance = new DBManager(context);
//                }
//            }
//        }
//        return mInstance;
//    }
    /**
     * 获取可读数据库
     */
    private  SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }
    /**
     * 获取可写数据库
     */

    private  SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
    public  void insertRoomCollection(RoomCollection roomCollection){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RoomCollectionDao roomCollectionDao=daoSession.getRoomCollectionDao();
        roomCollectionDao.insert(roomCollection);
    }
    public  void insertRoomCollectionList(List<RoomCollection> roomCollections){
        if(roomCollections==null||roomCollections.isEmpty())
            return;
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RoomCollectionDao roomCollectionDao=daoSession.getRoomCollectionDao();
        roomCollectionDao.insertInTx(roomCollections);
    }
    public  void deleteRoomCollection(List<RoomCollection> roomCollections){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RoomCollectionDao roomCollectionDao=daoSession.getRoomCollectionDao();
        roomCollectionDao.deleteInTx(roomCollections);
    }
    public  void updateRoomCollection(RoomCollection roomCollection){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RoomCollectionDao roomCollectionDao=daoSession.getRoomCollectionDao();
        roomCollectionDao.update(roomCollection);
    }
    public  List<RoomCollection> queryRoomCollectionList(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RoomCollectionDao roomCollectionDao=daoSession.getRoomCollectionDao();
        QueryBuilder<RoomCollection> qb=roomCollectionDao.queryBuilder();
        qb.where(RoomCollectionDao.Properties.Uid.eq(CommonPrefUtil.getStudentNumber()));
        return qb.list();
    }
    public  List<RoomCollection> queryRoomCollectionListByRoom(String room){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        RoomCollectionDao roomCollectionDao=daoSession.getRoomCollectionDao();
        QueryBuilder<RoomCollection> qb=roomCollectionDao.queryBuilder();
        qb.where(RoomCollectionDao.Properties.Room.eq(room)
                ,RoomCollectionDao.Properties.Uid.eq(CommonPrefUtil.getStudentNumber()));
        return qb.list();
    }
}