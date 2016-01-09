package com.twt.service.ui.news.collegenews;

import com.twt.service.bean.NewsList;
import com.twt.service.interactor.CollegeNewsInteractor;

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
    public void onFailure(String errorMsg) {
        view.setRefreshEnable(false);
        isLoadingMore = false;
        isRefreshing = false;
        view.toastMessage(errorMsg);
    }
}
