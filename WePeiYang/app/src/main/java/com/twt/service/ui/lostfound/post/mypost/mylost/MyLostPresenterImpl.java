package com.twt.service.ui.lostfound.post.mypost.mylost;

import android.preference.PreferenceFragment;

import com.twt.service.bean.Lost;
import com.twt.service.interactor.LostInteractor;
import com.twt.service.support.PrefUtils;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/5.
 */
public class MyLostPresenterImpl implements MyLostPresenter {

    private MyLostView view;
    private LostInteractor interactor;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    private int page;

    public MyLostPresenterImpl(MyLostView view, LostInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void refreshMyLostItems() {
        if (isRefreshing) {
            return;
        } else {
            isRefreshing = true;
            page = 1;
            interactor.getMyLostList(PrefUtils.getToken(), page);
        }
    }

    @Override
    public void loadMoreMyLostItems() {
        if (isLoadingMore) {
            return;
        } else {
            isLoadingMore = true;
            page += 1;
            interactor.getMyLostList(PrefUtils.getToken(), page);
        }
    }

    @Override
    public void onSuccess(Lost lost) {

    }

    @Override
    public void onFailure(RetrofitError error) {

    }

    @Override
    public void afterTokenRefreshed(String authorization) {

    }
}
