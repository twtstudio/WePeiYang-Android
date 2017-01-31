package com.twt.service.ui.lostfound.lost.details;

import com.twt.service.bean.LostDetails;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/20.
 */
public interface OnGetLostDetailsCallback {
    void onSuccess(LostDetails details);

    void onFailure(RetrofitError error);
}
