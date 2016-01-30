package com.twt.service.ui.schedule;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/1/27.
 */
public class FailureEvent {

    private RetrofitError retrofitError;

    public FailureEvent(RetrofitError retrofitError){
        this.retrofitError = retrofitError;
    }

    public RetrofitError getRetrofitError(){
        return retrofitError;
    }
}
