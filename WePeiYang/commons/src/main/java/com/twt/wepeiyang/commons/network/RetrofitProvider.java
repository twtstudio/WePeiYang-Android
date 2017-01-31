package com.twt.wepeiyang.commons.network;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * Created by retrox on 2017/1/25.
 */

public class RetrofitProvider {

    private Retrofit mRetrofit;

    private RetrofitProvider() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            if (message.startsWith("{")){
                Logger.json(message);
            }else {
                Platform.get().log(INFO, message, null);
            }
        });

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        SignInterceptor signInterceptor = new SignInterceptor();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(signInterceptor)
                .retryOnConnectionFailure(false)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://open.twtstudio.com/api/v1/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static class SignletonHolder{
        private static final RetrofitProvider INSTANCE = new RetrofitProvider();
    }

    public static Retrofit getRetrofit(){
        return SignletonHolder.INSTANCE.mRetrofit;
    }
}
