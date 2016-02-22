package com.twt.service.ui.lostfound.lost;

import com.twt.service.bean.Lost;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.LostInteractor;

import retrofit.RetrofitError;

/**
 * Created by Rex on 2015/8/2.
 */
public class LostPresenterImpl implements LostPresenter, onGetLostCallback {

    private LostView lostView;
    private LostInteractor interactor;
    private int page = 0;
    private boolean isRefershing = false;
    private boolean isLoadingMore = false;


    public LostPresenterImpl(LostView lostView, LostInteractor interactor) {
        this.lostView = lostView;
        this.interactor = interactor;
    }

    @Override
    public void refreshLostItems() {
        if (isRefershing) return;
        page = 1;
        isRefershing = true;
        interactor.getLostList(page);
    }

    @Override
    public void lostMoreLostItems() {
        if (isLoadingMore) return;
        page += 1;
        isLoadingMore = true;
        interactor.getLostList(page);
    }


    @Override
    public void onSuccess(Lost lost) {
        lostView.hideRefreshing();
        if (isRefershing) {
            isRefershing = false;
            lostView.refreshItems(lost.data);
        }
        if (isLoadingMore) {
            isLoadingMore = false;
            lostView.loadMoreItems(lost.data);
        }
    }

    @Override
    public void onFailure(RetrofitError error) {
        isLoadingMore = false;
        isRefershing = false;
        lostView.hideRefreshing();
        switch (error.getKind()) {
            case HTTP:
                RestError restError = (RestError) error.getBodyAs(RestError.class);
                if (restError != null) {
                    lostView.toastMessage(restError.message);
                }
                break;
            case NETWORK:
                lostView.toastMessage("无法连接到网络");
                break;
            case CONVERSION:
            case UNEXPECTED:
                throw error;
            default:
                throw new AssertionError("未知的错误类型：" + error.getKind());
        }
    }
}
