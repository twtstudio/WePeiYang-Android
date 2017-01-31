package com.twt.service.ui.lostfound.lost;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/22.
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
