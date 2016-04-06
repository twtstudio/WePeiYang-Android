package com.twt.service.ui.lostfound.post.mypost.myfound.event;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/6.
 */
public class GetMyFoundFailureEvent {
    private RetrofitError error;

    public GetMyFoundFailureEvent(RetrofitError error) {
        this.error = error;
    }

    public RetrofitError getError() {
        return error;
    }
}
