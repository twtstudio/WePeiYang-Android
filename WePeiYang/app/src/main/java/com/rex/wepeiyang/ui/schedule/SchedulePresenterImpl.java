package com.rex.wepeiyang.ui.schedule;

import com.rex.wepeiyang.bean.ClassTable;
import com.rex.wepeiyang.interactor.ScheduleInteractor;
import com.rex.wepeiyang.support.PrefUtils;

/**
 * Created by sunjuntao on 15/12/5.
 */
public class SchedulePresenterImpl implements SchedulePresenter, OnGetScheduleCallback {
    private ScheduleView view;
    private ScheduleInteractor interactor;

    public SchedulePresenterImpl(ScheduleView view, ScheduleInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void loadCourses() {
        view.showProgress();
        interactor.getSchedule(PrefUtils.getTjuUname(), PrefUtils.getTjuPasswd(), this);
    }

    @Override
    public void onSuccess(ClassTable classTable) {
        view.hideProgress();
        view.bindData(classTable);
    }

    @Override
    public void onFailure(String msg) {
        view.hideProgress();
        view.toastMessage(msg);
    }
}
