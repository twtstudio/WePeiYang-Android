package com.twt.service.ui.lostfound.found;

import com.twt.service.bean.Found;

import retrofit.RetrofitError;

/**
 * Created by RexSun on 15/8/16.
 */
public interface OnGetFoundCallback {
    void onSuccess(Found found);
    void onFailure(RetrofitError error);
}
