package com.twt.service.ui.gpa;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/15.
 */
public interface OnGetGpaCallback {
    void onSuccess(String gpaString);

    void onFailure(RetrofitError error);
}
