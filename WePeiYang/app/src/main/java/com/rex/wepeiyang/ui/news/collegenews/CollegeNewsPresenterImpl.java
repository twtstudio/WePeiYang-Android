package com.rex.wepeiyang.ui.news.collegenews;

import com.rex.wepeiyang.bean.NewsList;
import com.rex.wepeiyang.interactor.CollegeNewsInteractor;

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
            view.showRefreshing();
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
        if (isRefreshing) {
            isRefreshing = false;
            view.hideRefreshing();
            view.refreshItems(newsList.data);
        } else {
            isLoadingMore = false;
            view.loadMoreItems(newsList.data);
        }
    }

    @Override
    public void onFailure(String errorMsg) {
        isLoadingMore = false;
        isRefreshing = false;
        view.toastMessage(errorMsg);
    }
}
