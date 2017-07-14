package com.twtstudio.retrox.gpa;

import android.widget.Toast;

import com.kelin.mvvmlight.messenger.Messenger;
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
 * Created by retrox on 2017/1/17.
 * 每个模块的对外暴露的数据提供接口，同级无依赖的module无法调用，考虑router统一调用，暂不做实现
 * 上层依赖模块可以用普通方法调用
 * 包含一个数据的回调，使用RxJava自带的Action接口实现
 * 兼容lambda表达式
 * 内部也可以用
 */

public class GpaProvider {

    public static final String TOKEN_GPA_LOAD_FINISHED = "token_gpa_load_finished";

    //绑定生命周期用
    private RxAppCompatActivity mActivity;

    private Action1<GpaBean> action;

    private GpaProvider(RxAppCompatActivity activity) {
        mActivity = activity;
    }

    /**
     * default: not refresh the cache
     */
    public void getData() {
        getData(true);
    }

    /**
     * get from cache or network
     * @param update get from network and update the cache?
     */
    public void getData(boolean update) {
        GpaCacheProvider gpaCacheProvider = CacheProvider.getRxCache()
                .using(GpaCacheProvider.class);

        gpaCacheProvider.getGpaAuto(RetrofitProvider.getRetrofit()
                .create(GpaApi.class)
                .getGpa(), new DynamicKey("gpa"), new EvictDynamicKey(update))
                .subscribeOn(Schedulers.io())
                .doOnNext(gpaBeanReply -> Logger.d(gpaBeanReply.toString()))
                .map(Reply::getData)
                .map(MyGpaBean::getData)
                .compose(mActivity.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gpaBean -> {
                    //提供模块内的刷新服务，因为数据的bus是不能跨module的
                    Messenger.getDefault().send(gpaBean, TOKEN_GPA_LOAD_FINISHED);
                    if (action != null) {
                        if (gpaBean.data.size()!=0){
                            action.call(gpaBean);
                        }else {
                            Toast.makeText(mActivity, "数据出现问题,可尝试关闭退学或在GPA界面手动刷新...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, throwable -> new RxErrorHandler(mActivity).call(throwable.getCause()));

    }

    public static GpaProvider init(RxAppCompatActivity rxActivity) {
        return new GpaProvider(rxActivity);
    }

    public GpaProvider registerAction(Action1<GpaBean> action) {
        this.action = action;
        return this;
    }

}
