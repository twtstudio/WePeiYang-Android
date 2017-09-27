package com.twt.service.network.api;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * Created by chen on 2017/8/25.
 */

public class ApiPostClient {
    protected Retrofit mRetrofit;
    protected Retrofit mRetrofit2;
    private static final String bdAPI = "http://119.75.213.61/";
    private static final String baidu="https://www.baidu.com/";
    private static final String API = "http://202.113.5.133/";
    private static Interceptor requestInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder();
            Request request = builder.build();
            return chain.proceed(request);
        }
    };

    private ApiPostClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (message.startsWith("{")) {
                    Logger.json(message);
                } else {
                    Platform.get().log(INFO, message, null);
                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(API)
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mRetrofit2 = new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(bdAPI)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    private static class SingletonHolder {
        private static final ApiPostClient INSTANCE = new ApiPostClient();
    }

    public static Retrofit getRetrofit() {
        return SingletonHolder.INSTANCE.mRetrofit;
    }

    private static class SingletonHolder2 {
        private static final ApiPostClient INSTANCE = new ApiPostClient();
    }

    public static Retrofit getRetrofit2(){
        return SingletonHolder.INSTANCE.mRetrofit2;
    }


}
