package com.rex.wepeiyang.ui.login;

import com.rex.wepeiyang.bean.Login;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/1/1.
 */
public interface OnLoginCallback {
    void onSuccess(Login login);
    void onFailure(RetrofitError retrofitError);
}
