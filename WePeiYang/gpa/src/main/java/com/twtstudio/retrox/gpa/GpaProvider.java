package com.twtstudio.retrox.gpa;

import com.kelin.mvvmlight.messenger.Messenger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.network.ApiErrorHandler;
import com.twt.wepeiyang.commons.network.ApiResponse;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import rx.Notification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/1/17.
 * 每个模块的对外暴露的数据提供接口，同级无依赖的module无法调用，考虑router统一调用，暂不做实现
 * 上层依赖模块可以用普通方法调用
 * 包含一个数据的回调，使用RxJava自带的Action接口实现
 * 兼容lambda表达式
 * 内部也可以用
 */

public class GpaProvider  {

    public static final String TOKEN_GPA_LOAD_FINISHED = "token_gpa_load_finished";

    //绑定生命周期用
    private RxAppCompatActivity mActivity;

    private Action1<GpaBean> action;

    private GpaProvider(RxAppCompatActivity activity) {
        mActivity = activity;
    }

//    public GpaProvider(RxAppCompatActivity activity, Action1<GpaBean> action) {
//        mActivity = activity;
//        this.action = action;
//    }

    public void getData() {
        Observable<Notification<GpaBean>> gpaObservable =
                RetrofitProvider.getRetrofit()
                        .create(GpaApi.class)
                        .getGpa()
                        .subscribeOn(Schedulers.io())
                        .compose(mActivity.bindToLifecycle())
                        .map(ApiResponse::getData)
                        .materialize().share();

        gpaObservable.filter(Notification::isOnNext)
                .map(Notification::getValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gpaBean -> {
                    //提供模块内的刷新服务，因为数据的bus是不能跨module的
                    Messenger.getDefault().send(gpaBean, TOKEN_GPA_LOAD_FINISHED);
                    if (action != null) {
                        action.call(gpaBean);
                    }
                });

        ApiErrorHandler handler = new ApiErrorHandler(mActivity);

        handler.handleError(gpaObservable.filter(Notification::isOnError)
                .map(Notification::getThrowable));

    }

    public static GpaProvider init(RxAppCompatActivity rxActivity){
        return new GpaProvider(rxActivity);
    }

    public GpaProvider registerAction(Action1<GpaBean> action){
        this.action = action;
        return this;
    }

}
