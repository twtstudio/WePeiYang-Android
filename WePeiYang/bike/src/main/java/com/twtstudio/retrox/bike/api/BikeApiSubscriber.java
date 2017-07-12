package com.twtstudio.retrox.bike.api;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.twtstudio.retrox.bike.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import es.dmoral.toasty.Toasty;
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

    public BikeApiSubscriber(Context context, OnNextListener<T> listener,OnErrorListener errorListener) {
        mContext = context;
        mOnNextListener = listener;
        mOnErrorListener = errorListener;
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

    public void setOnErrorListener(OnErrorListener mOnErrorListener) {
        this.mOnErrorListener = mOnErrorListener;
    }

    @Override
    public void onError(Throwable e) {

        CrashReport.postCatchedException(e);

        if (mOnErrorListener != null) {
            mOnErrorListener.onError(e);
            return;
        }

        if (e instanceof ConnectException) {
            toastMessage("网络中断，请检查您的网络状态");
        } else if (e instanceof SocketTimeoutException) {
            toastMessage("网络连接超时");
        } else if (e instanceof HttpException){
            HttpException exception = (HttpException) e;
            Logger.e(exception, "http_error");
            try {
                String errorJson = exception.response().errorBody().string();
                Logger.e(errorJson);
                JSONObject errJsonObject = new JSONObject(errorJson);
                int errcode = errJsonObject.getInt("errno");
                String message = errJsonObject.getString("errmsg");
                Logger.e("错误码：" + errcode + "  message:" + message);
                Toasty.error(mContext, "错误：" + message +" TAT...", Toast.LENGTH_SHORT).show();

            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
            toastMessage("Http错误"+((HttpException) e).code());
        }
        else if (e instanceof BIkeApiException){
            toastMessage("错误: " + e.getMessage());
        }else {
            toastMessage(e.getMessage());
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
