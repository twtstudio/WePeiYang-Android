package com.twt.service.home.common.oneItem;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.kelin.mvvmlight.base.ViewModel;
import com.twt.service.api.ApiClient;
import com.twt.service.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rx.Notification;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/1/16.
 */

public class OneInfoViewModel extends AndroidViewModel implements ViewModel {
    

    /**
     * fields 与UI绑定的可变数据源
     */
    public final MutableLiveData<String> imageUrl = new MutableLiveData<>();

    public final MutableLiveData<String> content = new MutableLiveData<>();

    public final MutableLiveData<String> author = new MutableLiveData<>();

    public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    /**
     * 这里暂时没啥乱用
     */
    public class ViewStyle {
        public final ObservableBoolean progressRefreshing = new ObservableBoolean(true);
    }

    public OneInfoViewModel(@NonNull Application application) {
        super(application);
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
//                        .compose(mFragment.bindToLifecycle())
                        .materialize().share();

        oneInfoOb.filter(Notification::isOnNext)
                .map(Notification::getValue)
                .map(oneInfoBean -> oneInfoBean.hpEntity)
                .doOnNext(hpEntityBean -> {
                    imageUrl.setValue(hpEntityBean.strOriginalImgUrl);
                    content.setValue(hpEntityBean.strContent);
                    author.setValue(hpEntityBean.strAuthor);
                })
                .subscribe();

    }
}
