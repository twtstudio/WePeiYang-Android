package com.twt.service.rxsrc.api;

import rx.functions.Func1;

/**
 * Created by jcy on 2016/8/7.
 */

public class ReadResponseTransformer<T> implements Func1<ApiResponse<T>, T> {
    @Override
    public T call(ApiResponse<T> tApiResponse) {
        if (tApiResponse.getErr_code()!=-1){
            throw new ApiException(tApiResponse);
        }
        return tApiResponse.getData();
    }
}
