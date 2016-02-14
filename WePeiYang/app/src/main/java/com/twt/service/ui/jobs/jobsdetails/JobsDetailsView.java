package com.twt.service.ui.jobs.jobsdetails;

import com.twt.service.bean.Jobs;

/**
 * Created by sunjuntao on 16/2/13.
 */
public interface JobsDetailsView {
    void showProgress();
    void hideProgress();
    void bindData(Jobs jobs);
    void toastMessage(String message);
}
