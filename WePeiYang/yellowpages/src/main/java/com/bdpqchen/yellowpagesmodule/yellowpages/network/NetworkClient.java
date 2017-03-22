package com.bdpqchen.yellowpagesmodule.yellowpages.network;



import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by chen on 16-11-30.
 */

public class NetworkClient {

    private ApiOfYellowPages mService;

    private NetworkClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit mRetrofit = new Retrofit.Builder()
//                .baseUrl("https://raw.githubusercontent.com/bdpqchen/YellowPagesModule/master/data/")
                .baseUrl("https://open.twtstudio.com/api/v1/yellowpage/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(ApiOfYellowPages.class);

    }

    private static class SingletonHolder {
        private static final NetworkClient INSTANCE = new NetworkClient();
    }

    public static NetworkClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Subscription getDataList(Subscriber subscriber){
        return mService.getDataList()
                .compose(RxSchedulersHelper.io_main())
                .subscribe(subscriber);

    }

    public Subscription getDatabaseVersion(Subscriber subscriber) {
        return mService.getDbVersion()
                .compose(RxSchedulersHelper.io_main())
                .subscribe(subscriber);
    }

    public Subscription submitFeedback(Subscriber subscriber, int mFeedbackType, String mPhone, String mName) {
        return mService.submit(mFeedbackType + "", mPhone, mName)
                .compose(RxSchedulersHelper.io_main())
                .subscribe(subscriber);

    }

}
