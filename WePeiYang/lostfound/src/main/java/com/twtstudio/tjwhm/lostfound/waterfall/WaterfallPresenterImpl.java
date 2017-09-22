package com.twtstudio.tjwhm.lostfound.waterfall;

import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tjwhm on 2017/7/5.
 **/

public class WaterfallPresenterImpl implements WaterfallContract.WaterfallPresenter {
    WaterfallContract.WaterfallView waterfallView;
    WaterfallApi waterfallApi;

    public WaterfallPresenterImpl(WaterfallContract.WaterfallView waterfallView) {
        this.waterfallView = waterfallView;
    }

    @Override
    public void setWaterfallData(WaterfallBean waterfallBean) {
        waterfallView.setWaterfallData(waterfallBean);
    }

    @Override
    public void loadWaterfallData(String lostOrFound, int page) {
        waterfallApi = RetrofitProvider.getRetrofit().create(WaterfallApi.class);
        waterfallApi.loadWaterData(lostOrFound, String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setWaterfallData, new RxErrorHandler());
    }

    @Override
    public void loadWaterfallDataWithType(String lostOrFound, int page, int type) {
        if (type == -1) {
            loadWaterfallData(lostOrFound, page);
        } else {
            waterfallApi = RetrofitProvider.getRetrofit().create(WaterfallApi.class);
            waterfallApi.loadWaterfallDataWithType(lostOrFound, String.valueOf(page), String.valueOf(type))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setWaterfallData, new RxErrorHandler());
        }
    }


}
