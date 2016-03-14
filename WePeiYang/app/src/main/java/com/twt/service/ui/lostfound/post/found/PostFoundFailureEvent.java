package com.twt.service.ui.lostfound.post.found;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/3/14.
 */
public class PostFoundFailureEvent {
    private RetrofitError error;

    public PostFoundFailureEvent(RetrofitError error) {
        this.error = error;
    }

    public RetrofitError getError() {
        return error;
    }
}
