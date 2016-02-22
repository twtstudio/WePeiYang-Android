package com.twt.service.ui.lostfound.found;

import com.twt.service.bean.Found;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.FoundInteractor;

import retrofit.RetrofitError;

/**
 * Created by RexSun on 15/8/16.
 */
public class FoundPresenterImpl implements FoundPresenter, OnGetFoundCallback {


    private FoundView view;

    private FoundInteractor interactor;
    private int page;

    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;

    public FoundPresenterImpl(FoundView view, FoundInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }


    @Override
    public void refreshFoundItems() {
        if (isRefreshing) return;
        isRefreshing = true;
        page = 1;
        interactor.getFoundList(page);
    }

    @Override
    public void loadMoreFoundItems() {
        if (isLoadingMore) {
            return;
        } else {
            isLoadingMore = true;
            page += 1;
            interactor.getFoundList(page);
        }
    }

    @Override
    public void onSuccess(Found found) {
        view.hideRefreshing();
        if (isRefreshing) {
            view.refreshItems(found.data);
            isRefreshing = false;
        }
        if (isLoadingMore) {
            view.loadMoreItems(found.data);
            isLoadingMore = false;
        }
    }

    @Override
    public void onFailure(RetrofitError error) {
        isLoadingMore = false;
        isRefreshing = false;
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
}
