package com.twt.wepeiyang.commons.network;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.ReplaySubject;

/**
 * Created by retrox on 2017/1/20.
 */

public class ApiErrorHandler {

    private Context mContext;

    public ApiErrorHandler(Context context) {
        mContext = context;
    }

    public void handleError(Observable<Throwable> throwableObservable) {

        ReplaySubject<Throwable> ioErrorHandler = ReplaySubject.create();
        ReplaySubject<Throwable> apiErrorHandler = ReplaySubject.create();
        ReplaySubject<Throwable> throwableReplaySubject = ReplaySubject.create();

        throwableObservable = throwableObservable.distinct()
                .takeUntil(throwable -> throwable.getCause()==null);

        throwableObservable.subscribe(ioErrorHandler);
        throwableObservable.subscribe(apiErrorHandler);
        throwableObservable.subscribe(throwableReplaySubject);

        throwableReplaySubject.subscribe(Throwable::printStackTrace);

        ioErrorHandler.filter(throwable -> throwable instanceof IOException)
                .cast(IOException.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((error) -> {
                    Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                    Logger.e(error,"error");
                },Throwable::printStackTrace);

        apiErrorHandler.filter(throwable -> throwable instanceof ApiException)
                .cast(ApiException.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    /**
                     * test code
                     */
                    Toast.makeText(mContext, e.error_code, Toast.LENGTH_SHORT).show();
                    Logger.e(e,"error");
//                    switch (e.error_code){
//                        case 10000:
//                        case 10001:
//                        case 10002:
//                        case 10004:
//                            view.toastMessage("请重新登录");
//                            PrefUtils.setLogin(false);
//                            PrefUtils.removeToken();
//                            view.startLoginActivity();
//                            break;
//                        case 10003:
//                            Log.e("refresh", "refresh");
//                            interactor.refreshToken(PrefUtils.getToken(), this);
//                            break;
//                        case 20001:
//                            view.toastMessage("请绑定办公网帐号");
//                            view.startBindActivity();
//                            break;
//                        default:
//                            view.toastMessage(error.message);
//                            break;
//                    }
                });

    }
}
