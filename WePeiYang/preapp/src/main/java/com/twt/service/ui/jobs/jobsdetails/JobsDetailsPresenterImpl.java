package com.twt.service.ui.jobs.jobsdetails;

import android.view.View;

import com.twt.service.bean.Jobs;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.JobsInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/13.
 */
public class JobsDetailsPresenterImpl implements JobsDetailsPresenter, OnGetJobsDetailsCallback {

    private JobsDetailsView view;
    private JobsInteractor interactor;

    public JobsDetailsPresenterImpl(JobsDetailsView view, JobsInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onSuccess(Jobs jobs) {
        view.hideProgress();
        view.bindData(jobs);
    }

    @Override
    public void onFailure(RetrofitError error) {
        view.hideProgress();
        switch (error.getKind()) {
            case HTTP:
                RestError restError = (RestError)error.getBodyAs(RestError.class);
                if (restError!=null){
                    view.toastMessage(restError.message);
                }
                break;

            case NETWORK:
                view.toastMessage("无法连接到网络");
                break;

            case CONVERSION:
            case UNEXPECTED:
                throw error;

            default:
                throw new AssertionError("未知的错误类型：" + error.getKind());
        }

    }

    @Override
    public void getJobsDetails(int id) {
        view.showProgress();
        interactor.getJobsDetails(id);
    }
}
