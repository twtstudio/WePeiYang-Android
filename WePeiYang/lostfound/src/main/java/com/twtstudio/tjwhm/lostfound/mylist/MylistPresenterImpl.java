package com.twtstudio.tjwhm.lostfound.mylist;

import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twtstudio.tjwhm.lostfound.base.BaseBean;
import com.twtstudio.tjwhm.lostfound.base.CallbackBean;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tjwhm on 2017/7/7.
 **/

public class MylistPresenterImpl implements MylistContract.MylistPresenter {
    MylistContract.MylistView mylistView;
    MylistApi mylistApi;

    public MylistPresenterImpl(MylistContract.MylistView mylistView) {
        this.mylistView = mylistView;
    }

    @Override
    public void setMylistData(MylistBean mylistBean) {
        mylistView.setMylistData(mylistBean);
    }

    @Override
    public void loadMylistData(String lostOrFound, int page) {
        mylistApi = RetrofitProvider.getRetrofit().create(MylistApi.class);
        mylistApi.loadMylistData(lostOrFound, String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setMylistData, new RxErrorHandler());
    }

    @Override
    public void turnStatus(int id) {
        mylistApi = RetrofitProvider.getRetrofit().create(MylistApi.class);
        mylistApi.turnStatus(String.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::turnStatusSuccessCallBack, new RxErrorHandler());
    }

    @Override
    public void turnStatusSuccessCallBack(CallbackBean callbackBean) {
        mylistView.turnStatusSuccessCallBack();
    }
}
