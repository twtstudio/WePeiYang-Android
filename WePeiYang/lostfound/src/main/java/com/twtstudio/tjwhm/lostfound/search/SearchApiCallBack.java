package com.twtstudio.tjwhm.lostfound.search;

import rx.Subscriber;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public abstract class SearchApiCallBack<M> extends Subscriber<M> {


    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(M m) {
        onSuccess(m);
    }
}
