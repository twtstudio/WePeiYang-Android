package com.example.caokun.fellowsearch.model;

/**
 * Created by caokun on 2017/2/22.
 */

import com.example.caokun.fellowsearch.api.ApiException;
import com.example.caokun.fellowsearch.api.ApiResponse;

import rx.functions.Func1;

public class FellowResponseTransformer<T> implements Func1<ApiResponse<T>, T> {
    @Override
    public T call(ApiResponse<T> apiResponse) {
        if (apiResponse.getError_code()!=0){
            throw new ApiException(apiResponse);
        }
        return apiResponse.getData();
    }
}