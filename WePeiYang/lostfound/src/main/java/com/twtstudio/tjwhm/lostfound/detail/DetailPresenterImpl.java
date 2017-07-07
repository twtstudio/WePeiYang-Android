package com.twtstudio.tjwhm.lostfound.detail;

import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tjwhm on 2017/7/7.
 **/

public class DetailPresenterImpl implements DetailContract.DetailPresenter {

    DetailContract.DetailView detailView;
    DetailApi detailApi;

    public DetailPresenterImpl(DetailContract.DetailView detailView) {
        this.detailView = detailView;
    }

    @Override
    public void loadDetailData(int id) {
        detailApi = RetrofitProvider.getRetrofit().create(DetailApi.class);
        detailApi.loadDetailData(String.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setDetailData,new RxErrorHandler());
    }

    @Override
    public void setDetailData(DetailBean detailData) {
        detailView.setDetailData(detailData);
    }
}
