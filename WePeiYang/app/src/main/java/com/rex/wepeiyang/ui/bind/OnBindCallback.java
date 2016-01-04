package com.rex.wepeiyang.ui.bind;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/1/1.
 */
public interface OnBindCallback {
    void onSuccess();
    void onFailure(RetrofitError error);
}
