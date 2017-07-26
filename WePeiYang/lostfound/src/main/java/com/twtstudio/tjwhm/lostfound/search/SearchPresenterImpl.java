package com.twtstudio.tjwhm.lostfound.search;

import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallBean;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public class SearchPresenterImpl implements SearchContract.SearchPresenter {
    SearchContract.SearchUIView searchUIView;
    SearchApi searchApi;

    public SearchPresenterImpl(SearchContract.SearchUIView searchUIView) {
        this.searchUIView = searchUIView;
    }

    @Override
    public void loadSearchData(String keyword, int page) {
        searchApi = RetrofitProvider.getRetrofit().create(SearchApi.class);
        searchApi.loadSearchData(keyword, String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setSearchData, new RxErrorHandler());
    }

    @Override
    public void setSearchData(WaterfallBean waterfallBean) {
        searchUIView.setSearchData(waterfallBean);
    }
}
