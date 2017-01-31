package com.twt.service.ui.jobs;

import com.twt.service.bean.JobsList;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.JobsInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/13.
 */
public class JobsPresenterImpl implements JobsPresenter, OnGetJobsListCallback {

    private JobsView view;
    private JobsInteractor interactor;
    private int page = 1;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;

    public JobsPresenterImpl(JobsView view, JobsInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }


    @Override
    public void onSuccess(JobsList jobsList) {
        view.hideRefreshing();
        if (isRefreshing) {
            isRefreshing = false;
            view.refresh(jobsList);
        }
        if (isLoadingMore) {
            isLoadingMore = false;
            view.loadMore(jobsList);
        }
    }

    @Override
    public void onFailure(RetrofitError error) {
        isRefreshing = false;
        isLoadingMore = false;
        view.hideRefreshing();
        switch (error.getKind()) {
            case HTTP:
                RestError restError = (RestError) error.getBodyAs(RestError.class);
                if (restError != null) {
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
    public void refresh() {
        isRefreshing = true;
        page = 1;
        interactor.getJobsList(page);
    }

    @Override
    public void loadMore() {
        isLoadingMore = true;
        page += 1;
        interactor.getJobsList(page);
    }
}
