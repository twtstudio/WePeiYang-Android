package com.rex.wepeiyang.ui.gpa;

import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.interactor.GpaInteractor;
import com.rex.wepeiyang.support.PrefUtils;

/**
 * Created by sunjuntao on 15/11/22.
 */
public class GpaPresenterImpl implements GpaPresenter, OnGetGpaCallback {

    private GpaView view;
    private GpaInteractor interactor;

    public GpaPresenterImpl(GpaView view, GpaInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void getGpa() {
        view.showProgress();
        String tjuuname = PrefUtils.getTjuUname();
        String tjupasswd = PrefUtils.getTjuPasswd();
        interactor.getGpa(tjuuname, tjupasswd, this);
    }

    @Override
    public void onSuccess(Gpa gpa) {
        view.hideProgress();
        view.bindData(gpa);
    }

    @Override
    public void onFailure(String errorMsg) {
        view.hideProgress();
        view.toastMessage(errorMsg);
    }
}
