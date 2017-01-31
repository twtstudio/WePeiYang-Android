package com.twt.service.ui.date;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/5/3.
 */
public interface DatingPresenter {
    void tokenRefresh();

    void onTokenRefreshFailure(RetrofitError error);
}
