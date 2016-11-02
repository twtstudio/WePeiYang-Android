package com.twt.service.rxsrc.api;

import com.twt.service.api.Api;
import com.twt.service.rxsrc.model.read.Detail;
import com.twt.service.rxsrc.model.read.ReadToken;
import com.twt.service.support.PrefUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
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
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static android.R.attr.tag;

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

    private ReadResponseTransformer mResponseTransformer;

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
                .baseUrl("http://takooctopus.com/yuepeiyang/public/api/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(ReadApi.class);

        mTokenHelper = new ReadAuthHelper(mService);
        mResponseTransformer = new ReadResponseTransformer<>();
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
                    .addHeader("Authorization", PrefUtils.getReadToken());

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
     *
     * @param tag        presenter，需继承common包里面的presenter，自动绑定订阅关系
     * @param subscriber 订阅者，也是在presenter中初始化
     * @param wpy_token  传入的wpytoken
     */
    public void getReadToken(Object tag, Subscriber subscriber, String wpy_token) {
        Subscription subscription = mService.getReadToken(wpy_token)
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void searchBooks(Object tag, Subscriber subscriber, String info) {
        Subscription subscription = mService.searchBooks(info)
                .retryWhen(mTokenHelper)
//                .retryWhen(observable -> observable.flatMap(new Func1<Throwable, Observable<?>>() {
//                    @Override
//                    public Observable<?> call(Throwable throwable) {
//                        if (throwable instanceof SocketTimeoutException){
//                            return mService.searchBooks(info)
//                                    .map(mResponseTransformer)
//                                    .compose(ApiUtils.applySchedulers())
//                                    .doOnNext(new Action1() {
//                                        @Override
//                                        public void call(Object o) {
//
//                                        }
//                                    });
//                        }
//                        return Observable.error(throwable);
//                    }
//                }))
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getBookDetail(Object tag, Subscriber subscriber, String id) {
        Subscription subscription = mService.getBookDetail(id)
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getBanner(Object tag, Subscriber subscriber){
        Subscription subscription = mService.getBanner("5")
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getRecommendedList(Object tag, Subscriber subscriber, String count){
        Subscription subscription = mService.getRecommendedList(count)
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getReviewList(Object tag, Subscriber subscriber, String count){
        Subscription subscription = mService.getReviewList(count)
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getStarReaderList(Object tag, Subscriber subscriber, String count){
        Subscription subscription = mService.getStarReaderList(count)
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void addLike(Object tag, Subscriber subscriber, String id){
        Subscription subscription = mService.addLike(id)
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void delLike(Object tag, Subscriber subscriber, String id){
        Subscription subscription = mService.delLike(id)
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getBookShelf(Object tag, Subscriber subscriber){
        Subscription subscription = mService.getBookShelf()
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void delBookInShelf(Object tag, Subscriber subscriber, String[] id){
        Subscription subscription = mService.delBookShelf(id)
                .retryWhen(mTokenHelper)
                .map(mResponseTransformer)
                .compose(ApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }
}
