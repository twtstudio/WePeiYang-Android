package com.example.caokun.fellowsearch.model;

/**
 * Created by caokun on 2017/2/19.
 */
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class FellowApiClient {

    Retrofit mRetrofit;
    FellowApi fellowApi;
    private FellowResponseTransformer mfellowResponseTransformer;
    protected Map<Object, CompositeSubscription> mSubscriptionsMap = new HashMap<>();
    public FellowApiClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
//               好像没啥用 .addInterceptor(sRequestInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        mRetrofit=new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://120.27.115.59/test_laravel/public/index.php/api/Fellow/")
                .build();
        fellowApi =mRetrofit.create(FellowApi.class);
        mfellowResponseTransformer=new FellowResponseTransformer();
    }
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
     * 添加订阅关系，同时rxjava自动发起网络请求Su
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


    public void getProvience(Object tag,Subscriber subscriber){
        Subscription subscription=fellowApi.getProvince()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(mfellowResponseTransformer)
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }
    public void getStudent(Object tag,Subscriber subscriber,String province,String institute,String major,String senior){
        Subscription subscription=fellowApi.getStudent(province,institute,major,senior)
                                           .subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread())
                .map(mfellowResponseTransformer)
                                           .subscribe(subscriber);
        addSubscription(tag, subscription);

    }
    public void getInstitute(Object tag,Subscriber subscriber,String province){
        Subscription subscription=fellowApi.getInstitute(province)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(mfellowResponseTransformer)
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }
    public void getMajor(Object tag,Subscriber subscriber,String province,String institute ){
        Subscription subscription=  fellowApi.getMajor(province,institute)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(mfellowResponseTransformer)
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }
    public void getSenior(Object tag,Subscriber subscriber,String province){
        Subscription subscription= fellowApi.getSenior(province)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(mfellowResponseTransformer)
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }
    private static class SingletonHolder {
        private static final FellowApiClient INSTANCE = new FellowApiClient();
    }

    /**
     * APIclient的单例模式
     *
     * @return 返回APIclient的单例
     */
    public static FellowApiClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
