package com.twt.service.ui.news.collegenews;

import com.twt.service.bean.NewsList;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.CollegeNewsInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/19.
 */
public class CollegeNewsPresenterImpl implements CollegeNewsPresenter, OnGetCollegeNewsCallback {
    private CollegeNewsView view;
    private CollegeNewsInteractor interactor;
    private int page = 1;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;

    public CollegeNewsPresenterImpl(CollegeNewsView view, CollegeNewsInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void refreshItems() {
        if (isRefreshing) {
            return;
        } else {
            isRefreshing = true;
            page = 1;
            interactor.getCollegeNewslist(page, this);
        }
    }

    @Override
    public void loadMoreItems() {
        if (isLoadingMore) {
            return;
        } else {
            isLoadingMore = true;
            page += 1;
            interactor.getCollegeNewslist(page, this);
        }
    }

    @Override
    public void onSuccess(NewsList newsList) {
        view.setRefreshEnable(false);
        if (isRefreshing) {
            isRefreshing = false;
            view.refreshItems(newsList.data);
        } else {
            isLoadingMore = false;
            view.loadMoreItems(newsList.data);
        }
    }

    @Override
    public void onFailure(RetrofitError error) {
        view.setRefreshEnable(false);
        isLoadingMore = false;
        isRefreshing = false;
        switch (error.getKind()) {
            case HTTP:
                // TODO get message from getResponse()'s body or HTTP status
                RestError restError = (RestError)error.getBodyAs(RestError.class);
                if (restError!=null){
                    view.toastMessage(restError.message);
                }
                break;

            case NETWORK:
                // TODO get message from getCause()'s message or just declare "network problem"
                view.toastMessage("无法连接到网络");
                break;

            case CONVERSION:
            case UNEXPECTED:
                throw error;

            default:
                throw new AssertionError("未知的错误类型：" + error.getKind());
        }
    }
}
