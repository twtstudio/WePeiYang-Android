package com.twtstudio.retrox.bike.api;


import com.twtstudio.retrox.bike.utils.PrefUtils;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by jcy on 2016/8/22.
 */

public class ReadAuthHelper implements Func1<Observable<? extends Throwable>, Observable<?>>{
    private ReadApi mService;

    public ReadAuthHelper(ReadApi mService) {
        this.mService = mService;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (throwable instanceof HttpException){
                    HttpException exception = (HttpException) throwable;
                    if (exception.code()==400||exception.code()==403){
                        return mService.getReadToken(PrefUtils.getToken())
                                .doOnNext(readTokenApiResponse -> PrefUtils.setReadToken(readTokenApiResponse.getData().token));
                    }
                }

                return Observable.error(throwable);
            }
        });
    }
}
