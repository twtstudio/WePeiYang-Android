package com.rex.wepeiyang.ui.feedback;

import com.rex.wepeiyang.bean.RestError;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/1/8.
 */
public interface OnFeedbackCallback {
    void onSuccess();
    void onFailure(RetrofitError retrofitError);
}
