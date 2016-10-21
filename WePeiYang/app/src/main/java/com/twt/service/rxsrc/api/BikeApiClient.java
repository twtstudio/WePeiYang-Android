package com.twt.service.rxsrc.api;

import android.util.Log;


import com.twt.service.rxsrc.model.BikeAnnouncement;
import com.twt.service.rxsrc.model.BikeAuth;
import com.twt.service.rxsrc.model.BikeCard;
import com.twt.service.rxsrc.model.BikeRecord;
import com.twt.service.rxsrc.model.BikeStation;
import com.twt.service.rxsrc.model.BikeUserInfo;
import com.twt.service.support.PrefUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
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
 * Created by jcy on 2016/8/6.
 */

public class BikeApiClient {

    protected Retrofit mRetrofit;

    protected Map<Object, CompositeSubscription> mSubscriptionsMap = new HashMap<>();

    private BikeApi mService;

    private AuthHelper mTokenHelper;

    private BikeApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(sRequsetInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://bike.twtstudio.com/api.php/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(BikeApi.class);
        mTokenHelper = new AuthHelper(mService);
    }

    private static class SingletonHolder {
        private static final BikeApiClient INSTANCE = new BikeApiClient();
    }

    public static BikeApiClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    protected static Interceptor sRequsetInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originRequset = chain.request();
            //header
            Request.Builder builder = originRequset.newBuilder()
                    .addHeader("Accept", "application/json");
            //auth---no token
            List<String> pathList = originRequset.url().pathSegments();
            if (!pathList.contains("auth")) {
                //add token
                if (originRequset.body() instanceof FormBody) {
                    FormBody.Builder newFormBody = new FormBody.Builder();
                    FormBody oldFormBody = (FormBody) originRequset.body();
                    for (int i = 0; i < oldFormBody.size(); i++) {
                        newFormBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
                    }
                    String bike_token = PrefUtils.getBikeToken();
                    //bike_token.replace("+"," ");
                    newFormBody.addEncoded("auth_token", bike_token);
                    Log.d("api", "intercept: " + PrefUtils.getBikeToken());
                    builder.method(originRequset.method(), newFormBody.build());
                }

            }
            Request request = builder.build();

            return chain.proceed(request);
        }
    };

    public void unSubscribe(Object tag) {
        if (mSubscriptionsMap.containsKey(tag)) {
            CompositeSubscription subscriptions = mSubscriptionsMap.get(tag);
            subscriptions.unsubscribe();
            mSubscriptionsMap.remove(tag);
        }
    }

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

    public void getBikeToken(Object tag, Subscriber subscriber, String wpy_token) {
        Subscription subscription = mService.getBikeToken(wpy_token)
                .retryWhen(mTokenHelper)
                .map(new BikeResponseTransformer<BikeAuth>())
                .compose(BikeApiUtils.<BikeAuth>applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getBikeCard(Object tag, Subscriber subscriber, String num) {
        Subscription subscription = mService.getBikeCard(num)
                .retryWhen(mTokenHelper)
                .map(new BikeResponseTransformer<List<BikeCard>>())
                .compose(BikeApiUtils.<List<BikeCard>>applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getStationStatus(Object tag, Subscriber subscriber, String id) {
        Subscription subscription = mService.getStationStaus(id)
                .map(new BikeResponseTransformer<List<BikeStation>>())
                .compose(BikeApiUtils.<List<BikeStation>>applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void bindBikeCard(Object tag, Subscriber subscriber, String id, String sign) {
        Subscription subscription = mService.bindBikeCard(id, sign)
                .retryWhen(mTokenHelper)
                .map(new BikeResponseTransformer<String>())
                .compose(BikeApiUtils.<String>applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getUserInfo(Object tag, Subscriber subscriber) {
        Subscription subscription = mService.getUserInfo("fake")
                .retryWhen(mTokenHelper)
                .map(new BikeResponseTransformer<BikeUserInfo>())
                .compose(BikeApiUtils.<BikeUserInfo>applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void cacheStationStatus(Object tag, Subscriber subscriber) {
        Subscription subscription = mService.cacheStationStaus()
                .repeatWhen(v -> v.delay(90, TimeUnit.SECONDS))
                .map(new BikeResponseTransformer<List<BikeStation>>())
                .compose(BikeApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getAnnouncement(Object tag, Subscriber subscriber) {
        Subscription subscription = mService.getBikeAnnouncement()
                .map(new BikeResponseTransformer<List<BikeAnnouncement>>())
                .compose(BikeApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void getBikeRecord(Object tag, Subscriber subscriber, String month) {
        Subscription subscription = mService.getBikeRecord(month)
                .map(new BikeResponseTransformer<List<BikeRecord>>())
                .compose(BikeApiUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }
}
