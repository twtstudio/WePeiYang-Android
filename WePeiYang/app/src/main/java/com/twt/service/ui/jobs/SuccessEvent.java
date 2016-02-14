package com.twt.service.ui.jobs;

import com.twt.service.bean.JobsList;

/**
 * Created by sunjuntao on 16/2/14.
 */
public class SuccessEvent {
    private JobsList jobsList;

    public SuccessEvent(JobsList jobsList) {
        this.jobsList = jobsList;
    }

    public JobsList getJobsList() {
        return jobsList;
    }
}
