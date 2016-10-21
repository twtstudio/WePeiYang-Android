package com.twt.service.rxsrc.api;

import rx.functions.Func1;

/**
 * Created by jcy on 2016/8/7.
 */

public class BikeResponseTransformer<T> implements Func1<BikeApiResponse<T>, T> {
    @Override
    public T call(BikeApiResponse<T> tBikeApiResponse) {
        if (tBikeApiResponse.getErrno() != 0) {
            throw new ApiException(tBikeApiResponse);
        }
        return tBikeApiResponse.getData();
    }
}
