package com.twt.service.bike.api;

import com.twt.service.bike.model.BikeAuth;
import com.twt.service.support.PrefUtils;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by jcy on 2016/8/22.
 */

public class AuthHelper implements Func1<Observable<? extends Throwable>, Observable<?>>{
    private BikeApi mService;

    public AuthHelper(BikeApi mService) {
        this.mService = mService;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (throwable instanceof HttpException){
                    HttpException exception = (HttpException) throwable;
                    if (exception.code()==403){
                        return mService.getBikeToken(PrefUtils.getTokenForBike())
                                .doOnNext(bikeAuthBikeApiResponse -> PrefUtils.setBikeToken(bikeAuthBikeApiResponse.getData().token));
                    }
                }

                return Observable.error(throwable);
            }
        });
    }
}
