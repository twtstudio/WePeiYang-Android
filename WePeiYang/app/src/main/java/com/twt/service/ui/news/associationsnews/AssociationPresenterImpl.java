package com.twt.service.ui.news.associationsnews;

import com.twt.service.bean.NewsList;
import com.twt.service.interactor.AssociationInteractor;

/**
 * Created by sunjuntao on 15/11/19.
 */
public class AssociationPresenterImpl implements AssociationPresenter, OnGetAssociationCallback {

    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    private int page;
    private AssociationInteractor interactor;
    private AssociationView view;

    public AssociationPresenterImpl(AssociationView view, AssociationInteractor interactor) {
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
            interactor.getAssociationList(page, this);
        }
    }

    @Override
    public void loadMoreItems() {
        if (isLoadingMore) {
            return;
        } else {
            isLoadingMore = true;
            page += 1;
            interactor.getAssociationList(page, this);
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
        view.showToast(errorMsg);
    }
}
