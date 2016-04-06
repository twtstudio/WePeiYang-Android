package com.twt.service.ui.lostfound.post.mypost.myfound;

import com.twt.service.bean.Found;
import com.twt.service.interactor.FoundInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/5.
 */
public class MyFoundPresenterImpl implements MyFoundPresenter {

    private MyFoundView view;
    private FoundInteractor interactor;

    public MyFoundPresenterImpl(MyFoundView view, FoundInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void refreshMyFoundItems() {

    }

    @Override
    public void loadMoreMyFoundItems() {

    }

    @Override
    public void onSuccess(Found found) {

    }

    @Override
    public void onFailure(RetrofitError error) {

    }
}
