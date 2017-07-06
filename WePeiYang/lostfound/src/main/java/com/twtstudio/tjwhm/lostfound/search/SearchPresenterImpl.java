package com.twtstudio.tjwhm.lostfound.search;

import com.twtstudio.tjwhm.lostfound.base.BasePrsenterImpl;
import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallBean;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public class SearchPresenterImpl extends BasePrsenterImpl implements SearchContract.SearchPresenter {
    SearchContract.SearchUIView searchUIView;
    SearchApiClient searchApiClient = new SearchApiClient(this);
    private CompositeSubscription compositeSubscription;
    SearchApi searchApi;

    public SearchPresenterImpl(SearchContract.SearchUIView searchUIView) {
        this.searchUIView = searchUIView;
    }

    @Override
    public void loadSearchData(String keyword) {
        searchApi = searchApiClient.searchRetrofit().create(SearchApi.class);
        addSubscription(searchApi.loadSearchData(keyword),
                new SearchApiCallBack<WaterfallBean>() {
                    @Override
                    public void onSuccess(WaterfallBean model) {
                        setSearchData(model);
                    }

                    @Override
                    public void onFailure(String msg) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @Override
    public void setSearchData(WaterfallBean waterfallBean) {
        searchUIView.setSearchData(waterfallBean);
    }
}
