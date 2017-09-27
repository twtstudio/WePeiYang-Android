package com.twt.service.network.api;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.network.SignInterceptor;
import com.twt.wepeiyang.commons.network.UaInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chen on 2017/7/12.
 */

public class ApiGetClient {
    protected Retrofit mRetrofit;
    protected Api mService;

    private ApiGetClient() {
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(message->{
            if (message.startsWith("{")){
                Logger.json(message);
            }else {
                Platform.get().log(Platform.INFO,message,null);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        SignInterceptor signInterceptor = new SignInterceptor();
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
               .addInterceptor(loggingInterceptor)
               .addInterceptor(signInterceptor)
                .addInterceptor(new UaInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://open.twtstudio.com/api/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mService=mRetrofit.create(Api.class);

    }
    public Api getService(){
        return mService;
    }
    private static class SingletonHolder{
        private static final ApiGetClient INSTANCE=new ApiGetClient();
    }

    public static Retrofit getRetrofit() {
        return SingletonHolder.INSTANCE.mRetrofit;
    }
}
