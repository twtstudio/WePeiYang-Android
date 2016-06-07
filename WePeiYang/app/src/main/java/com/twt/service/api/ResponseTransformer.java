package com.twt.service.api;

import rx.functions.Func1;

/**
 * Created by huangyong on 16/5/19.
 */
public class ResponseTransformer<T> implements Func1<ApiResponse<T>, T> {
    @Override
    public T call(ApiResponse<T> tAPIResponse) {
        if (tAPIResponse.getErrorCode() != -1) {
            throw new ApiException(tAPIResponse);
        }
        return tAPIResponse.getData();
    }
}
