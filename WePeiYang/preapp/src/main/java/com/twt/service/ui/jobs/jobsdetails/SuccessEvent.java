package com.twt.service.ui.jobs.jobsdetails;

import com.twt.service.bean.Jobs;

/**
 * Created by sunjuntao on 16/2/13.
 */
public class SuccessEvent {

    private Jobs jobs;

    public SuccessEvent(Jobs jobs) {
        this.jobs = jobs;
    }

    public Jobs getJobs() {
        return jobs;
    }
}
