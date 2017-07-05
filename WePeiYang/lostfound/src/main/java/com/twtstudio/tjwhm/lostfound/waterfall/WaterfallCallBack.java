package com.twtstudio.tjwhm.lostfound.waterfall;

import rx.Subscriber;

/**
 * Created by tjwhm on 2017/7/5.
 **/

public abstract class WaterfallCallBack<M> extends Subscriber<M> {

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();

    @Override
    public void onCompleted() {
        System.out.println("WaterfallCallBack.onCompleted"+"abcdef");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        System.out.println("WaterfallCallBack.onError"+"abcdef"+e + e.getStackTrace().toString());
    }

    @Override
    public void onNext(M m) {
        onSuccess(m);
        System.out.println("WaterfallCallBack.onNext"+"abcdef");
    }
}
