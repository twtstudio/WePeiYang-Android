package com.twt.service.ui.bus;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/5/3.
 */
public interface BusPresenter {
    void tokenRefresh();

    void onTokenRefreshFailure(RetrofitError retrofitError);
}
