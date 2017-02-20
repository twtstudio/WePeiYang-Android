package com.twtstudio.retrox.schedule.model;

import android.app.job.JobScheduler;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/4.
 */

public class ClassTableProvider {

    private RxAppCompatActivity mRxActivity;

    private Action1<ClassTable> mAction1;

    private ClassTableProvider(RxAppCompatActivity rxActivity) {
        mRxActivity = rxActivity;
    }

    public void getData() {
        getData(false);
    }

    public void getData(boolean update) {

        CacheProvider.getRxCache().using(ScheduleCacheApi.class)
                .getClassTableAuto(RetrofitProvider.getRetrofit().create(ScheduleApi.class)
                .getClassTable(), new DynamicKey("classTable"), new EvictDynamicKey(update))
                .subscribeOn(Schedulers.io())
                .doOnNext(reply -> Logger.d(reply.toString()))
                .map(Reply::getData)
//                .map(ClassTable::getData)
                .doOnNext(ClassTable::getData)
                .compose(mRxActivity.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(classTable -> {
                    if (mAction1 != null) {
                        mAction1.call(classTable);
                    }
                },new RxErrorHandler(mRxActivity));

    }


    public static ClassTableProvider init(RxAppCompatActivity activity) {
        return new ClassTableProvider(activity);
    }

    public ClassTableProvider registerAction(Action1<ClassTable> action1) {
        this.mAction1 = action1;
        return this;
    }
}
