package com.twtstudio.retrox.one;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.cache.CacheProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.Reply;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * Created by retrox on 2017/2/11.
 */

public class OneApiProvider {

    private Retrofit retrofit;

    private OneApi oneApi;

    private OneCacheApi oneCacheApi;

    public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);


    private OneApiProvider() {
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
                .retryOnConnectionFailure(false)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://v3.wufazhuce.com:8000/api/hp/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        oneApi = retrofit.create(OneApi.class);

        oneCacheApi = CacheProvider.getRxCache().using(OneCacheApi.class);
    }

    private static class SingleTonHolder {
        private static final OneApiProvider INSTANCE = new OneApiProvider();
    }

    public static OneApiProvider getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    public void getIdlist(Action1<OneDetailBean.DataBean> dataBeanAction1){

        Observable.just(Calendar.getInstance())
                .subscribeOn(Schedulers.io())
                .map(Calendar::getTime)
                .map(dateFormate::format)
                .flatMap(s -> oneCacheApi.getIdListAuto(oneApi.getOneIdList(),new DynamicKey(s)))
                .map(Reply::getData)
                .map(IdList::getData)
                .flatMap(Observable::from)
                .flatMap(s -> oneCacheApi.getOneDetailAuto(oneApi.getOneDetail(s),new DynamicKey(s)))
                .map(Reply::getData)
                .map(OneDetailBean::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataBeanAction1,Throwable::printStackTrace);

    }
}
