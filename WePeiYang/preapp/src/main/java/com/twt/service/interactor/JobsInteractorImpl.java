package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.Jobs;
import com.twt.service.bean.JobsList;
import com.twt.service.ui.jobs.jobsdetails.FailureEvent;
import com.twt.service.ui.jobs.jobsdetails.SuccessEvent;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 16/2/13.
 */
public class JobsInteractorImpl implements JobsInteractor {
    @Override
    public void getJobsDetails(int id) {
        ApiClient.getJobsDetails(id, new Callback<Jobs>() {
            @Override
            public void success(Jobs jobs, Response response) {
                EventBus.getDefault().post(new SuccessEvent(jobs));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FailureEvent(error));
            }
        });
    }

    @Override
    public void getJobsList(int page) {
        ApiClient.getJobsList(page, new Callback<JobsList>() {
            @Override
            public void success(JobsList jobsList, Response response) {
                EventBus.getDefault().post(new com.twt.service.ui.jobs.SuccessEvent(jobsList));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new com.twt.service.ui.jobs.FailureEvent(error));
            }
        });
    }
}
