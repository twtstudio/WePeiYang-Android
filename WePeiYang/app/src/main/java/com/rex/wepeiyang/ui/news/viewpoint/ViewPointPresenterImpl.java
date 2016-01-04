package com.rex.wepeiyang.ui.news.viewpoint;

import com.rex.wepeiyang.bean.NewsItem;
import com.rex.wepeiyang.bean.NewsList;
import com.rex.wepeiyang.interactor.ViewPointInteractor;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/18.
 */
public class ViewPointPresenterImpl implements ViewPointPresenter, OnGetViewPointCallback {

    private ViewPointView view;
    private ViewPointInteractor interactor;
    private int page;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;

    public ViewPointPresenterImpl(ViewPointView view, ViewPointInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void refreshItems() {
        if (isRefreshing) {
            return;
        } else {
            page = 1;
            isRefreshing = true;
            interactor.getViewPoints(page, this);
        }
    }

    @Override
    public void loadMoreItems() {
        if (isLoadingMore){
            return;
        }else {
            page += 1;
            isLoadingMore = true;
            interactor.getViewPoints(page, this);
        }

    }

    @Override
    public void onSuccess(NewsList newsList) {
        if (newsList.data.size() == 0) {
            isLoadingMore = false;
            isRefreshing = false;
            view.toastMessage("没有新闻了orz");
            return;
        }
        if (isRefreshing){
            isRefreshing = false;
            view.refreshItems(newsList.data);
        }else {
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
