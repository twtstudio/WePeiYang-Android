package com.twt.service.ui.schedule;

import com.twt.service.bean.ClassTable;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/12/5.
 */
public interface OnGetScheduleCallback {
    void onSuccess(String classTableString);

    void onFailure(RetrofitError retrofitError);
}
