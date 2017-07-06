package com.twtstudio.tjwhm.lostfound.release;

import rx.Subscriber;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public abstract class ReleaseApiCallBack<M> extends Subscriber<M> {

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        System.out.println("ReleaseApiCallBack.onError"+"abcdef");
        e.printStackTrace();
    }

    @Override
    public void onNext(M m) {
        onSuccess(m);
    }
}
