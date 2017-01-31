package com.twt.service.ui.common;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/4.
 */
public class TokenRefreshFailureEvent {
    private RetrofitError error;

    public TokenRefreshFailureEvent(RetrofitError error) {
        this.error = error;
    }

    public RetrofitError getError() {
        return error;
    }
}
