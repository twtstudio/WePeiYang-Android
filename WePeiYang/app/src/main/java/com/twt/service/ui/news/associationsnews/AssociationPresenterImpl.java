package com.twt.service.ui.news.associationsnews;

import com.twt.service.bean.NewsList;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.AssociationInteractor;

import retrofit.RetrofitError;

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
    public void onFailure(RetrofitError error) {
        view.setRefreshEnable(false);
        isLoadingMore = false;
        isRefreshing = false;
        switch (error.getKind()) {
            case HTTP:
                // TODO get message from getResponse()'s body or HTTP status
                RestError restError = (RestError)error.getBodyAs(RestError.class);
                if (restError!=null){
                    view.showToast(restError.message);
                }
                break;

            case NETWORK:
                // TODO get message from getCause()'s message or just declare "network problem"
                view.showToast("无法连接到网络");
                break;

            case CONVERSION:
            case UNEXPECTED:
                throw error;

            default:
                throw new AssertionError("未知的错误类型：" + error.getKind());
        }
    }
}
