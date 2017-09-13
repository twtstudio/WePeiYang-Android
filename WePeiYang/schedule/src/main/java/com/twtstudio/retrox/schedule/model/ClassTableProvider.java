package com.twtstudio.retrox.schedule.model;

import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/4.
 */

public class ClassTableProvider {

    private RxAppCompatActivity mRxActivity;

    private Action1<ClassTable> mAction1;
    private Action2<ClassTable, CalendarDay> mAction2;

    private ClassTableProvider(RxAppCompatActivity rxActivity) {
        mRxActivity = rxActivity;
    }

    public void getData() {
        getData(false);
    }

    public void getData(CalendarDay calendarDay) {
        getData(false, calendarDay);
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
                    //存入学期开始时间
                    CommonPrefUtil.setStartUnix(Long.valueOf(classTable.data.term_start));

                    if (mAction1 != null) {
                        mAction1.call(classTable);
                    }
                }, throwable -> {
                    if (throwable.getCause() != null)
                        new RxErrorHandler(mRxActivity).call(throwable.getCause());
                });

    }

    public void getData(boolean update, CalendarDay calendarDay) {
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
                    //存入学期开始时间
                    CommonPrefUtil.setStartUnix(Long.valueOf(classTable.data.term_start));

                    if (mAction2 != null) {
                        mAction2.call(classTable, calendarDay);
                    }
                    if (mAction1 != null) {
                        mAction1.call(classTable);
                    }
                }, throwable -> {
                    if (throwable.getCause() != null)
                        new RxErrorHandler(mRxActivity).call(throwable.getCause());
                });

    }

    public static ClassTableProvider init(RxAppCompatActivity activity) {
        return new ClassTableProvider(activity);
    }

    public ClassTableProvider registerAction(Action1<ClassTable> action1) {
        this.mAction1 = action1;
        return this;
    }

    public ClassTableProvider registerAction2(Action2<ClassTable, CalendarDay> action2) {
        this.mAction2 = action2;
        return this;
    }
}
