package com.twt.service.rxsrc.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jcy on 16-10-22.
 */

public class ReadApiClient {

    protected Retrofit mRetrofit;

    protected Map<Object,CompositeSubscription> mSubscriptionsMap = new HashMap<>();

    private ReadApi mService;

    // TODO: 16-10-22 定制authhelper来实现readtoken的刷新
    private AuthHelper mTokenHelper;

    public ReadApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //todo:此处应该有添加token的拦截器
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(false)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        // TODO: 16-10-22 修改fakeURL为图书url
        mRetrofit = new Retrofit.Builder()
                .baseUrl("fakeurl")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(ReadApi.class);
    }

    private static class SingletonHolder {
        private static final ReadApiClient INSTANCE = new ReadApiClient();
    }

    /**
     * APIclient的单例模式
     * @return 返回APIclient的单例
     */
    public static ReadApiClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 取消订阅关系，类似与cancel网络请求，操作在presenter的destroy方法里面被调用
     * @param tag 传入presenter的实例
     */
    public void unSubscribe(Object tag) {
        if (mSubscriptionsMap.containsKey(tag)) {
            CompositeSubscription subscriptions = mSubscriptionsMap.get(tag);
            subscriptions.unsubscribe();
            mSubscriptionsMap.remove(tag);
        }
    }

    /**
     * 添加订阅关系，同时rxjava自动发起网络请求
     * @param tag presenter的实例
     * @param subscription 创建好的订阅关系
     */
    protected void addSubscription(Object tag, Subscription subscription) {
        if (tag == null) {
            return;
        }
        CompositeSubscription subscriptions;
        if (mSubscriptionsMap.containsKey(tag)) {
            subscriptions = mSubscriptionsMap.get(tag);
        } else {
            subscriptions = new CompositeSubscription();
        }
        subscriptions.add(subscription);
        mSubscriptionsMap.put(tag, subscriptions);
    }

}
