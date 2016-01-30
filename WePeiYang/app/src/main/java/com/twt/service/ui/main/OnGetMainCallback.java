package com.twt.service.ui.main;

import com.google.gson.JsonElement;
import com.twt.service.bean.Main;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/12/3.
 */
public interface OnGetMainCallback {
    void onSuccess(String mainString);
    void onFailure(RetrofitError retrofitError);
}
