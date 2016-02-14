package com.twt.service.ui.jobs;

import com.twt.service.bean.JobsList;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/13.
 */
public interface OnGetJobsListCallback {
    void onSuccess(JobsList jobsList);

    void onFailure(RetrofitError error);
}
