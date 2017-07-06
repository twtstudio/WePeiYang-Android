package com.twtstudio.tjwhm.lostfound.waterfall;

import com.twtstudio.tjwhm.lostfound.base.BasePrsenterImpl;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tjwhm on 2017/7/5.
 **/

public class WaterfallPresenterImpl extends BasePrsenterImpl implements WaterfallContract.WaterfallPresenter {
    WaterfallContract.WaterfallView waterfallView;
    WaterfallApiClient waterfallApiClient = new WaterfallApiClient(this);
    private CompositeSubscription compositeSubscription;
    WaterfallApi waterfallApi;

    public WaterfallPresenterImpl(WaterfallContract.WaterfallView waterfallView) {
        this.waterfallView = waterfallView;
    }

    @Override
    public void setWaterfallData(WaterfallBean waterfallBean) {
        waterfallView.setWaterfallData(waterfallBean);
    }

    @Override
    public void loadWaterfallData(String lostOrFound,int page) {
        waterfallApi = waterfallApiClient.waterfallRerofit().create(WaterfallApi.class);
        addSubscription(waterfallApi.loadWaterData(lostOrFound,String.valueOf(page)),
                new WaterfallCallBack<WaterfallBean>() {
                    @Override
                    public void onSuccess(WaterfallBean model) {
                        setWaterfallData(model);
                    }

                    @Override
                    public void onFailure(String msg) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }
//
//    public void addSubscription(Observable observable, Subscriber subscriber) {
//        if (compositeSubscription == null) {
//            compositeSubscription = new CompositeSubscription();
//        }
//        compositeSubscription.add(observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
//    }
}
