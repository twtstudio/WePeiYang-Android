package com.rex.wepeiyang.ui.news.associations;

import com.rex.wepeiyang.bean.NewsList;
import com.rex.wepeiyang.interactor.AssociationInteractor;

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
            view.showRefreshing();
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
            view.userFooter();
            interactor.getAssociationList(page, this);
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
            view.hideFooter();
            view.loadMoreItems(newsList.data);
        }
    }

    @Override
    public void onFailure(String errorMsg) {
        isLoadingMore = false;
        isRefreshing = false;
        view.showToast(errorMsg);
    }
}
