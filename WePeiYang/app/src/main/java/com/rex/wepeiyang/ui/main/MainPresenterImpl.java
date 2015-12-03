package com.rex.wepeiyang.ui.main;

import com.rex.wepeiyang.bean.Main;
import com.rex.wepeiyang.interactor.MainInteractor;

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
