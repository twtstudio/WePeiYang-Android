package com.twt.service.ui.jobs.jobsdetails;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/13.
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
