package com.rex.wepeiyang.ui.schedule;

import com.rex.wepeiyang.bean.ClassTable;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/12/5.
 */
public interface OnGetScheduleCallback {
    void onSuccess(ClassTable classTable);

    void onFailure(RetrofitError retrofitError);
}
