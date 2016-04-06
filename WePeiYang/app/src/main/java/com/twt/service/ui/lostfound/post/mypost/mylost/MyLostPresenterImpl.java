package com.twt.service.ui.lostfound.post.mypost.mylost;

import com.twt.service.bean.Lost;
import com.twt.service.interactor.LostInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/5.
 */
public class MyLostPresenterImpl implements MyLostPresenter {

    private MyLostView view;
    private LostInteractor interactor;

    public MyLostPresenterImpl(MyLostView view, LostInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void refreshMyLostItems() {

    }

    @Override
    public void loadMoreMyLostItems() {

    }

    @Override
    public void onSuccess(Lost lost) {

    }

    @Override
    public void onFailure(RetrofitError error) {

    }
}
