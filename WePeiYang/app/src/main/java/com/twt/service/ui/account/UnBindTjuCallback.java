package com.twt.service.ui.account;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/1/27.
 */
public interface UnBindTjuCallback {
    void onSuccess();
    void onFailure(RetrofitError retrofitError);
}
