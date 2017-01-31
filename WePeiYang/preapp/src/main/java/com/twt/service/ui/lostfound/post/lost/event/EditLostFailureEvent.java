package com.twt.service.ui.lostfound.post.lost.event;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/18.
 */
public class EditLostFailureEvent {
    private RetrofitError error;

    public EditLostFailureEvent(RetrofitError error) {
        this.error = error;
    }

    public RetrofitError getError() {
        return error;
    }
}
