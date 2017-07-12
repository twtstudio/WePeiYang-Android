package com.twtstudio.service.classroom.model;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangyulong on 7/10/17.
 */

public class ClassRoomProvider {
    private RxAppCompatActivity mRxActivity;

    private Action1<FreeRoom2> mAction1;
    ClassRoomProvider(RxAppCompatActivity mRxActivity){
        this.mRxActivity=mRxActivity;
    }
    public void getFreeClassroom(int buiding,  int week,  int time,  String token){
        CacheProvider.getRxCache().using(ClassRoomCacheApi.class)
                .getFreeRoomAuto(RetrofitProvider.getRetrofit().create(ClassRoomApi.class)
                .getFreeClassroom(buiding,week,time,token), new DynamicKey("Classroom/getClassroom"), new EvictDynamicKey(true))
                .subscribeOn(Schedulers.io())
                .doOnNext(reply -> Logger.d(reply.toString()))
                .map(Reply::getData)
                .doOnNext(FreeRoom2::getData)
                .compose(mRxActivity.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(freeRoom2 -> {

                },new RxErrorHandler(mRxActivity));

    }
    public void getAllCollectedClassroom(String token,int week){
        CacheProvider.getRxCache().using(ClassRoomCacheApi.class)
                .getAllCollectedClassroomAuto(RetrofitProvider.getRetrofit().create(ClassRoomApi.class)
                        .getAllCollectedClassroom(token,week), new DynamicKey("Classroom/showCollection"), new EvictDynamicKey(true))
                .subscribeOn(Schedulers.io())
                .doOnNext(reply -> Logger.d(reply.toString()))
                .map(Reply::getData)
                .doOnNext(CollectedRoom2::getData)
                .compose(mRxActivity.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collectedRoom2 -> {

                },new RxErrorHandler(mRxActivity));
    }
    public void collect(String buiding, String token){
        CacheProvider.getRxCache().using(ClassRoomCacheApi.class)
                .collectAuto(RetrofitProvider.getRetrofit().create(ClassRoomApi.class)
                        .collect(buiding,token), new DynamicKey("Classroom/roomCollection"), new EvictDynamicKey(true))
                .subscribeOn(Schedulers.io())
                .doOnNext(reply -> Logger.d(reply.toString()))
                .map(Reply::getData)
                .doOnNext(ClassRoomApiReaponse<collectApiResponse>::getData)
                .compose(mRxActivity.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collectApiResponseClassRoomApiReaponse -> {

                },new RxErrorHandler(mRxActivity));
    }
    public void cancelCollect(String token,String building){
        CacheProvider.getRxCache().using(ClassRoomCacheApi.class)
                .cancelCollectAuto(RetrofitProvider.getRetrofit().create(ClassRoomApi.class)
                        .cancelCollect(token, building), new DynamicKey("Classroom/removeCollection"), new EvictDynamicKey(true))
                .subscribeOn(Schedulers.io())
                .doOnNext(reply -> Logger.d(reply.toString()))
                .map(Reply::getData)
                .doOnNext(ClassRoomApiReaponse::getData)
                .compose(mRxActivity.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collectApiResponseClassRoomApiReaponse -> {

                },new RxErrorHandler(mRxActivity));
    }
    public ClassRoomProvider registerAction(Action1<FreeRoom2> action1) {
        this.mAction1 = action1;
        return this;
    }
    public static ClassRoomProvider init(RxAppCompatActivity rxAppCompatActivity){
        return new ClassRoomProvider(rxAppCompatActivity);
    }
}
