package com.rex.wepeiyang.ui.lostfound.lost;

import com.rex.wepeiyang.interactor.LostInteractor;

/**
 * Created by Rex on 2015/8/2.
 */
public class LostPresenterImpl implements LostPresenter, onGetLostCallback {

    private LostView lostView;
    private LostInteractor interactor;
    private int itemsPerPage = 20;
    private int page = 0;
    private boolean isRefershing = false;
    private boolean isLoadingMore = false;
    private boolean isFirstLoad = true;


    public LostPresenterImpl(LostView lostView, LostInteractor interactor) {
        this.lostView = lostView;
        this.interactor = interactor;
    }

    @Override
    public void refreshLostItems() {
        if (isRefershing) return;
        page = 0;
        isRefershing = true;
        lostView.showRefreshing();
        interactor.getLost();
    }

    @Override
    public void lostMoreLostItems() {
        if (isLoadingMore) return;
        page += 1;
        isLoadingMore = true;
        lostView.showRefreshing();
        interactor.getLost();

    }


    @Override
    public void onSuccess() {
        lostView.hideRefreshing();
    }

    @Override
    public void onFailure(String errorMsg) {
        isLoadingMore = false;
        isRefershing = false;
        lostView.toastMessage(errorMsg);
    }
}
