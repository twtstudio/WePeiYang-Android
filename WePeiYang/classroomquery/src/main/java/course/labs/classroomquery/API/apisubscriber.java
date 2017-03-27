package course.labs.classroomquery.API;

import android.content.Context;
import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by asus on 2017/1/27.
 */
public class apisubscriber<T> extends Subscriber<T> {
    protected Context mContext;

    protected onNextListener<T> mOnNextListener;


    protected onErrorListener mOnErrorListener;

    // TODO: 16-11-3 debug
    protected boolean isToastError = false;

    private static final String TAG = "ApiSubscriber";

    public apisubscriber(Context context, onNextListener<T> listener) {
        mContext = context;
        mOnNextListener = listener;
    }

    public apisubscriber(Context context, onNextListener<T> listener, onErrorListener errorListener) {
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

    public void setOnErrorListener(onErrorListener mOnErrorListener) {
        this.mOnErrorListener = mOnErrorListener;
    }

    @Override
    public void onError(Throwable e) {
        if (mOnErrorListener != null) {
            mOnErrorListener.OnError(e);

        }

        if (e instanceof ConnectException) {
            toastMessage("网络中断，请检查您的网络状态");
        } else if (e instanceof SocketTimeoutException) {
            toastMessage("网络连接超时");
        } else if (e instanceof HttpException){
            toastMessage("Http错误"+((HttpException) e).code());
        }
        else if (e instanceof ApiException){
            toastMessage("错误: " + e.getMessage());
        }else if(e instanceof NullPointerException){
            e.printStackTrace();
            toastMessage("对不起，没有数据");
        } else {
            if (isToastError){
                toastMessage(e.getMessage());
            }
            toastMessage("sorry....");
        }
    }

    private void toastMessage(String message) {
       // if (isToastError()) {
            Log.d(TAG, "异常识别 : " + message);

       // }
    }

    @Override
    public void onNext(T t) {
        if(t==null){
            System.out.println("OnNext"+" is null");
        }
        else{
            System.out.println("OnNext"+" is not null");
        }
        if (mOnNextListener != null) {

            mOnNextListener.OnNext(t);
        }

    }





}
