package com.twt.service.ui.main;

import com.twt.service.bean.Main;
import com.twt.service.interactor.MainInteractor;

/**
 * Created by sunjuntao on 15/12/3.
 */
public class MainPresenterImpl implements MainPresenter, OnGetMainCallback {

    private MainView view;
    private MainInteractor interactor;

    public MainPresenterImpl(MainView view, MainInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void loadData() {
        view.showProgress();
        interactor.getMain(this);
    }

    @Override
    public void onSuccess(Main main) {
        view.hideProgress();
        view.bindData(main);
    }

    @Override
    public void onFailure(String errorMsg) {
        view.hideProgress();
        view.toastMessage(errorMsg);
    }
}
