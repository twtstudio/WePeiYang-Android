package com.twt.service.ui.lostfound.post.lost.event;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/3/12.
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
