package com.rex.wepeiyang.ui.bind;

import com.rex.wepeiyang.interactor.BindInteractor;
import com.rex.wepeiyang.ui.common.NextActivity;

/**
 * Created by sunjuntao on 16/1/1.
 */
public class BindPresenterImpl implements BindPresenter, OnBindCallback {
    private BindView view;
    private BindInteractor interactor;
    private NextActivity nextActivity;

    public BindPresenterImpl(BindView view, BindInteractor interactor, NextActivity nextActivity) {
        this.view = view;
        this.interactor = interactor;
        this.nextActivity = nextActivity;
    }

    @Override
    public void bind(String tjuname, String tjupwd) {
        view.showProgress();
        interactor.bind(tjuname, tjupwd, this);
    }

    @Override
    public void onSuccess() {
        view.hindProgress();
        switch (nextActivity) {
            case Main:
                view.startMainActivity();
                break;
            case Gpa:
                view.startGpaActivity();
                break;
        }
    }

    @Override
    public void onFailure(String errorMsg) {
        view.hindProgress();
    }
}
