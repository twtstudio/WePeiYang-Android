package com.twtstudio.tjwhm.lostfound.waterfall;

import com.twt.wepeiyang.commons.network.RetrofitProvider;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tjwhm on 2017/7/5.
 **/

public class WaterfallApiClient {
    private WaterfallContract.WaterfallPresenter waterfallPresenter;
    private Retrofit retrofit;

    public WaterfallApiClient(WaterfallContract.WaterfallPresenter waterfallPresenter) {
        this.waterfallPresenter = waterfallPresenter;
    }

    public Retrofit waterfallRerofit(){
        retrofit = RetrofitProvider.getRetrofit();
        return retrofit;
    }
}
