package com.rex.wepeiyang.ui.gpa;

import com.google.gson.JsonElement;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/15.
 */
public interface OnGetGpaCallback {
    void onSuccess(JsonElement jsonElement);
    void onFailure(RetrofitError error);
}
