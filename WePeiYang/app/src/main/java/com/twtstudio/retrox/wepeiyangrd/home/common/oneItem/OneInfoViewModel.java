package com.twtstudio.retrox.wepeiyangrd.home.common.oneItem;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;


import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.wepeiyangrd.api.ApiClient;
import com.twtstudio.retrox.wepeiyangrd.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rx.Notification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/1/16.
 */

public class OneInfoViewModel implements ViewModel {

    /**
     * 用于绑定fragment的生命周期，还有context的提供
     */
    private BaseFragment mFragment;

    /**
     * fields 与UI绑定的可变数据源
     */
    public final ObservableField<String> imageUrl = new ObservableField<>();

    public final ObservableField<String> content = new ObservableField<>();

    public final ObservableField<String> author = new ObservableField<>();

    public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    /**
     * 这里暂时没啥乱用
     */
    public class ViewStyle{
        public final ObservableBoolean progressRefreshing = new ObservableBoolean(true);
    }

    public OneInfoViewModel(BaseFragment fragment) {
        mFragment = fragment;
        getData();
    }

    /**
     * Rx 风格的数据请求
     */
    private void getData() {
        Observable<Notification<OneInfoBean>> oneInfoOb =
                Observable.just(Calendar.getInstance())
                        .subscribeOn(Schedulers.io())
                        .map(Calendar::getTime)
                        .map(dateFormate::format)
                        .flatMap(s -> ApiClient.getService().getOneHpInfo(s))
                        .compose(mFragment.bindToLifecycle())
                        .materialize().share();

        oneInfoOb.filter(Notification::isOnNext)
                .map(Notification::getValue)
                .map(oneInfoBean -> oneInfoBean.hpEntity)
                .doOnNext(hpEntityBean -> {
                    imageUrl.set(hpEntityBean.strOriginalImgUrl);
                    content.set(hpEntityBean.strContent);
                    author.set(hpEntityBean.strAuthor);
                })
                .subscribe();

    }
}
