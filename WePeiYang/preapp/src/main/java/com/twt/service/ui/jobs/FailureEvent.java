package com.twt.service.ui.jobs;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/14.
 */
public class FailureEvent {
    private RetrofitError error;

    public FailureEvent(RetrofitError error) {
        this.error = error;
    }

    public RetrofitError getError() {
        return error;
    }
}
