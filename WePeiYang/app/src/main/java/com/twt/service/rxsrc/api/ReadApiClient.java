package com.twt.service.rxsrc.api;

import com.twt.service.support.PrefUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jcy on 16-10-22.
 *
 * @TwtStudio Mobile Develope Team
 */

public class ReadApiClient {

    protected Retrofit mRetrofit;

    protected Map<Object, CompositeSubscription> mSubscriptionsMap = new HashMap<>();

    private ReadApi mService;

    // TODO: 16-10-22 定制authhelper来实现readtoken的刷新
    private ReadAuthHelper mTokenHelper;

    public ReadApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(sRequestInterceptor)
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

        mTokenHelper = new ReadAuthHelper(mService);
    }

    private static class SingletonHolder {
        private static final ReadApiClient INSTANCE = new ReadApiClient();
    }

    /**
     * APIclient的单例模式
     *
     * @return 返回APIclient的单例
     */
    public static ReadApiClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 添加readtoken的拦截器
     */
    private static Interceptor sRequestInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request originRequest = chain.request();

            Request.Builder builder = originRequest
                    .newBuilder()
                    .addHeader("Authorization",PrefUtils.getBikeToken());

            Request request = builder.build();
            return chain.proceed(request);
        }
    };

    /**
     * 取消订阅关系，类似与cancel网络请求，操作在presenter的destroy方法里面被调用
     *
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
     *
     * @param tag          presenter的实例
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

    /**
     * 添加新功能时候照着这个写
     * @param tag presenter，需继承common包里面的presenter，自动绑定订阅关系
     * @param subscriber 订阅者，也是在presenter中初始化
     * @param wpy_token 传入的wpytoken
     */
    public void getReadToken(Object tag, Subscriber subscriber, String wpy_token) {
        Subscription subscription = mService.getReadToken(wpy_token)
                .retryWhen(mTokenHelper)
                .map(new ReadResponseTransformer<>())
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }


}
