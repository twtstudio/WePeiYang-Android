package com.twt.service.api;

import rx.functions.Func1;

/**
 * Created by huangyong on 16/5/19.
 */
public class ResponseTransformer<T> implements Func1<APIResponse<T>, T> {
    @Override
    public T call(APIResponse<T> tAPIResponse) {
        if (tAPIResponse.getErrorCode() != -1) {
            throw new APIException(tAPIResponse);
        }
        return tAPIResponse.getData();
    }
}
