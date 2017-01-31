package com.twt.service.ui.gpa.evalution;

import retrofit.RetrofitError;

/**
 * Created by tjliqy on 2017/1/15.
 */

public interface onPostEvaluateCallBack {
    void onSuccess(String message);
    void onFailure(RetrofitError error);
}
