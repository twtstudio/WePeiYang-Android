package com.twt.service.bike.api;

import android.content.Context;


import com.twt.service.bike.utils.ToastUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by jcy on 2016/8/7.
 */

public class BikeApiSubscriber<T> extends Subscriber<T> {
    protected Context mContext;

    protected OnNextListener<T> mOnNextListener;

    protected OnErrorListener mOnErrorListener;

    protected boolean isToastError = true;

    public BikeApiSubscriber(Context context, OnNextListener<T> listener) {
        mContext = context;
        mOnNextListener = listener;
    }

    public boolean isToastError() {
        return isToastError;
    }

    public void setToastError(boolean toastError) {
        isToastError = toastError;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (mOnErrorListener != null) {
            mOnErrorListener.onError(e);
            return;
        }

        if (e instanceof ConnectException) {
            toastMessage("网络中断，请检查您的网络状态");
        } else if (e instanceof SocketTimeoutException) {
            toastMessage("网络连接超时");
        } else if (e instanceof HttpException){
            toastMessage("Http错误"+((HttpException) e).code());
        }
        else {
            toastMessage("未知错误: " + e.toString());
            // TODO: 2016/8/1 log测试代码
            try {
                throw e;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private void toastMessage(String message) {
        if (isToastError()) {
            ToastUtils.showMessage(mContext, message);
        }
    }

    @Override
    public void onNext(T t) {
        if (mOnNextListener != null) {
            mOnNextListener.onNext(t);
        }
    }
}
