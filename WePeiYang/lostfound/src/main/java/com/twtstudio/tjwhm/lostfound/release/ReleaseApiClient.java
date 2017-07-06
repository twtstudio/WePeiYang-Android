package com.twtstudio.tjwhm.lostfound.release;

import com.twt.wepeiyang.commons.network.RetrofitProvider;

import retrofit2.Retrofit;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public class ReleaseApiClient {
    private Retrofit retrofit;

    public Retrofit releaseRetrofit(){
        retrofit = RetrofitProvider.getRetrofit();
        return retrofit;
    }

}
