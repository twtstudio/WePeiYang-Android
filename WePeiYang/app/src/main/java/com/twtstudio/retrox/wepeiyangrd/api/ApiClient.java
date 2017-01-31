package com.twtstudio.retrox.wepeiyangrd.api;


import com.annimon.stream.Stream;
import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.JniUtils;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * Created by retrox on 2016/11/25.
 */

public class ApiClient {

    private Retrofit mRetrofit;

    private Api mService;

    public ApiClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            if (message.startsWith("{")){
                Logger.json(message);
            }else {
                Platform.get().log(INFO, message, null);
            }
        });

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(sRequestInterceptor)
                .retryOnConnectionFailure(false)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

//        Gson gson = new GsonBuilder()
//                .registerTypeAdapterFactory(new ApiTypeAdapterFactory("data"))
//                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://open.twtstudio.com/api/v1/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(Api.class);
    }

    private static class SingletonHolder {
        private static final ApiClient INSTANCE = new ApiClient();
    }

    public static ApiClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static Api getService(){
        return SingletonHolder.INSTANCE.mService;
    }

    protected static Interceptor sRequestInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originRequest = chain.request();

            /**
             * deal with other requests (not twt requests)
             */
            if (!originRequest.url().host().equals("open.twtstudio.com")){
                return chain.proceed(originRequest);
            }

            HttpUrl newUrl = convert(originRequest.url());

            Request.Builder builder = originRequest.newBuilder()
//                    .addHeader("User-Agent", UserAgent.generate())
                    .addHeader("Authorization", "Bearer{"+ CommonPrefUtil.getToken()+"}")
                    .url(newUrl);

            Logger.d("token-->"+ CommonPrefUtil.getToken());

            return chain.proceed(builder.build());
        }

        HttpUrl convert(HttpUrl originUrl) {

            String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());

            List<String> keysList = new ArrayList<>(originUrl.queryParameterNames());
            keysList.add("t");
            Collections.sort(keysList);

            String keys = Stream.of(keysList).map(s1 -> {
                if ("t".equals(s1)){
                    s1 += timestamp;
                }else {
                    s1 += originUrl.queryParameter(s1);
                }
                return s1;
            }).reduce((value1, value2) -> value1 + value2).get();

            String sign = new String(Hex.encodeHex(DigestUtils.sha(JniUtils.getInstance().getAppKey() + keys + JniUtils.getInstance().getAppSecret()))).toUpperCase();

            return originUrl.newBuilder()
                    .addQueryParameter("t", timestamp)
                    .addQueryParameter("sign", sign)
                    .addQueryParameter("app_key", JniUtils.getInstance().getAppKey())
                    .build();
        }

    };
}
