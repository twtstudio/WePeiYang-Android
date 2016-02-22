package com.twt.service.ui.lostfound.lost;

import com.twt.service.bean.Lost;

import retrofit.RetrofitError;

/**
 * Created by Rex on 2015/8/2.
 */
public interface onGetLostCallback {
    void onSuccess(Lost lost);

    void onFailure(RetrofitError error);
}
