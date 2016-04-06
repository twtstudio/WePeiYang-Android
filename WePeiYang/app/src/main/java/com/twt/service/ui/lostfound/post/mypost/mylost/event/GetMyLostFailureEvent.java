package com.twt.service.ui.lostfound.post.mypost.mylost.event;

import com.twt.service.ui.lostfound.post.mypost.myfound.event.GetMyFoundFailureEvent;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/6.
 */
public class GetMyLostFailureEvent {
    private RetrofitError error;

    public GetMyLostFailureEvent(RetrofitError error) {
        this.error = error;
    }

    public RetrofitError getError() {
        return error;
    }
}
