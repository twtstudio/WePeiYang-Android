package com.twtstudio.tjwhm.lostfound.search;

import com.twt.wepeiyang.commons.network.RetrofitProvider;

import retrofit2.Retrofit;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public class SearchApiClient {
   private SearchContract.SearchPresenter searchPresenter;
    private Retrofit retrofit;

    public SearchApiClient(SearchContract.SearchPresenter searchPresenter) {
        this.searchPresenter = searchPresenter;
    }
    public Retrofit searchRetrofit(){
        retrofit = RetrofitProvider.getRetrofit();
        return retrofit;
    }
}
