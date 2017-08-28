package com.twtstudio.service.classroom.model;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.database.DBManager;
import com.twtstudio.service.classroom.database.RoomCollection;
import com.twtstudio.service.classroom.view.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangyulong on 7/10/17.
 */

public class ClassRoomProvider {
    private RxAppCompatActivity mRxActivity;

    private Action1<FreeRoom2> mAction1;


    ClassRoomProvider(RxAppCompatActivity mRxActivity) {
        this.mRxActivity = mRxActivity;
    }

    public void getFreeClassroom(int buiding, int week, int day, int time, String token, MainActivityViewModel viewModel) {
        CacheProvider.getRxCache().using(ClassRoomCacheApi.class)
                .getFreeRoomAuto(RetrofitProvider.getRetrofit().create(ClassRoomApi.class)
                        .getFreeClassroom(buiding, day, week, time, token), new DynamicKey("Classroom/getClassroom"), new EvictDynamicKey(true))
                .subscribeOn(Schedulers.io())
                .doOnNext(reply -> Logger.d(reply.toString()))
                .map(Reply::getData)
                .doOnNext(FreeRoom2::getData)
                .compose(mRxActivity.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(freeRoom2 -> {
                    if (mAction1 != null) {
                        freeRoom2.setTime(time);
                        freeRoom2.setBuilding(buiding);
                        mAction1.call(freeRoom2);
                    }
                }, throwable -> {
                    RxErrorHandler rxErrorHandler=new RxErrorHandler(mRxActivity);
                    rxErrorHandler.call(throwable.getCause());
                    rxErrorHandler.showHttpErrorOnMainActivity(viewModel,throwable.getCause());
                    rxErrorHandler.stopLoading(viewModel);
                });

    }
    public void getFreeClassroom(int buiding, int week, int day, int time, String token) {
        CacheProvider.getRxCache().using(ClassRoomCacheApi.class)
                .getFreeRoomAuto(RetrofitProvider.getRetrofit().create(ClassRoomApi.class)
                        .getFreeClassroom(buiding, day, week, time, token), new DynamicKey("Classroom/getClassroom"), new EvictDynamicKey(true))
                .subscribeOn(Schedulers.io())
                .doOnNext(reply -> Logger.d(reply.toString()))
                .map(Reply::getData)
                .doOnNext(FreeRoom2::getData)
                .compose(mRxActivity.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(freeRoom2 -> {
                    if (mAction1 != null) {
                        freeRoom2.setTime(time);
                        freeRoom2.setBuilding(buiding);
                        mAction1.call(freeRoom2);
                    }
                }, throwable -> new RxErrorHandler(mRxActivity).call(throwable.getCause()));

    }
    public void getAllCollectedClassroom() {
        Observable.just(CommonPrefUtil.getStudentNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mRxActivity.bindToLifecycle())
                .subscribe((studentNumber) -> {
                    List<RoomCollection> roomCollections = DBManager.getInstance(mRxActivity).queryRoomCollectionList();
                    List<FreeRoom2.FreeRoom> freeRooms = new ArrayList<>();
                    FreeRoom2 freeRoom2 = new FreeRoom2();
                    if (roomCollections != null) {
                        for (RoomCollection roomCollection : roomCollections)
                            freeRooms.add(roomCollection.toFreeRoom());
                        freeRoom2.setData(freeRooms);
                    }
                    if (!freeRoom2.getData().isEmpty())
                        this.mAction1.call(freeRoom2);
                });
    }


    public void addCollectedClassRoom(RoomCollection roomCollection) {
        Observable.just(roomCollection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mRxActivity.bindToLifecycle())
                .subscribe((collection) -> {
                    collection.setUid(CommonPrefUtil.getStudentNumber());
                    collection.setCollection(true);
                    DBManager.getInstance(mRxActivity).insertRoomCollection(collection);
                });
    }

    public void deleteCollectedClassRoom(RoomCollection roomCollection) {
        Observable.just(roomCollection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mRxActivity.bindToLifecycle())
                .subscribe((collection) -> {
                    if (collection.getUid().equals(CommonPrefUtil.getStudentNumber())) {
//                        DBManager.getInstance(mRxActivity).deleteRoomCollection(collection);
                        List<RoomCollection> roomCollections = DBManager
                                .getInstance(mRxActivity).queryRoomCollectionListByRoom(collection.getRoom());
                        collection.setCollection(false);
                        DBManager.getInstance(mRxActivity).deleteRoomCollection(roomCollections);
                    }
                });
    }
//    public void getAllCollectedClassroom(String token, int week) {
//        CacheProvider.getRxCache().using(ClassRoomCacheApi.class)
//                .getAllCollectedClassroomAuto(RetrofitProvider.getRetrofit().create(ClassRoomApi.class)
//                        .getAllCollectedClassroom(token, week), new DynamicKey("Classroom/showCollection"), new EvictDynamicKey(true))
//                .subscribeOn(Schedulers.io())
//                .doOnNext(reply -> Logger.d(reply.toString()))
//                .map(Reply::getData)
//                .doOnNext(CollectedRoom2::getData)
//                .compose(mRxActivity.bindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(collectedRoom2 -> {
//
//                }, throwable -> new RxErrorHandler(mRxActivity).call(throwable.getCause()));
//    }

//    public void collect(String buiding, String token) {
//        CacheProvider.getRxCache().using(ClassRoomCacheApi.class)
//                .collectAuto(RetrofitProvider.getRetrofit().create(ClassRoomApi.class)
//                        .collect(buiding, token), new DynamicKey("Classroom/roomCollection"), new EvictDynamicKey(true))
//                .subscribeOn(Schedulers.io())
//                .doOnNext(reply -> Logger.d(reply.toString()))
//                .map(Reply::getData)
//                .doOnNext(ClassRoomApiReaponse<collectApiResponse>::getData)
//                .compose(mRxActivity.bindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(collectApiResponseClassRoomApiReaponse -> {
//
//                }, throwable -> new RxErrorHandler(mRxActivity).call(throwable.getCause()));
//    }
//
//    public void cancelCollect(String token, String building) {
//        CacheProvider.getRxCache().using(ClassRoomCacheApi.class)
//                .cancelCollectAuto(RetrofitProvider.getRetrofit().create(ClassRoomApi.class)
//                        .cancelCollect(token, building), new DynamicKey("Classroom/removeCollection"), new EvictDynamicKey(true))
//                .subscribeOn(Schedulers.io())
//                .doOnNext(reply -> Logger.d(reply.toString()))
//                .map(Reply::getData)
//                .doOnNext(ClassRoomApiReaponse::getData)
//                .compose(mRxActivity.bindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(collectApiResponseClassRoomApiReaponse -> {
//
//                }, throwable -> new RxErrorHandler(mRxActivity).call(throwable.getCause()));
//    }

    public ClassRoomProvider registerAction(Action1<FreeRoom2> action1) {
        this.mAction1 = action1;
        return this;
    }

    public static ClassRoomProvider init(RxAppCompatActivity rxAppCompatActivity) {
        return new ClassRoomProvider(rxAppCompatActivity);
    }
}
