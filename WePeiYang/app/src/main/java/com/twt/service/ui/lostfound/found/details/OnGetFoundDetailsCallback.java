package com.twt.service.ui.lostfound.found.details;

import com.twt.service.bean.FoundDetails;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/20.
 */
public interface OnGetFoundDetailsCallback {
    void onSuccess(FoundDetails details);

    void onFailure(RetrofitError error);
}
