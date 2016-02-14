package com.twt.service.ui.jobs.jobsdetails;

import com.twt.service.bean.Jobs;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/13.
 */
public interface OnGetJobsDetailsCallback {
    void onSuccess(Jobs jobs);
    void onFailure(RetrofitError retrofitError);
}
