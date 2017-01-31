package com.twt.service.ui.lostfound.post.found.event;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/26.
 */
public class UploadFailureEvent {
    private RetrofitError error;

    public UploadFailureEvent(RetrofitError error) {
        this.error = error;
    }

    public RetrofitError getError() {
        return error;
    }
}
