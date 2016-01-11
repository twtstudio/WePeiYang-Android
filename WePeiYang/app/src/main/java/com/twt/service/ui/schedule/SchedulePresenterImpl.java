package com.twt.service.ui.schedule;

import com.twt.service.bean.*;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.ScheduleInteractor;
import com.twt.service.support.PrefUtils;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/12/5.
 */
public class SchedulePresenterImpl implements SchedulePresenter, OnGetScheduleCallback, OnRefreshTokenCallback {
    private ScheduleView view;
    private ScheduleInteractor interactor;

    public SchedulePresenterImpl(ScheduleView view, ScheduleInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void loadCourses() {
        view.showProgress();
        interactor.getSchedule(PrefUtils.getToken(), this);
    }

    @Override
    public void onSuccess(ClassTable classTable) {
        view.hideProgress();
        view.bindData(classTable);
    }

    @Override
    public void onFailure(RetrofitError retrofitError) {
        view.hideProgress();
        RestError error = (RestError)retrofitError.getBodyAs(RestError.class);
        if (error != null) {
            switch (error.error_code) {
                case 10000:
                    view.toastMessage("请重新登录");
                    PrefUtils.setLogin(false);
                    PrefUtils.setToken(null);
                    view.startLoginActivity();
                    break;
                case 10001:
                    view.toastMessage("请重新登录");
                    PrefUtils.setLogin(false);
                    PrefUtils.setToken(null);
                    view.startLoginActivity();
                    break;
                case 10002:
                    view.toastMessage("请重新登录");
                    PrefUtils.setLogin(false);
                    PrefUtils.setToken(null);
                    view.startLoginActivity();
                    break;
                case 10003:
                    interactor.refreshToken(PrefUtils.getToken(), this);
                    break;
                case 20001:
                    view.toastMessage("请绑定办公网帐号");
                    view.startBindActivity();
                    break;
                default:
                    view.toastMessage(error.message);
                    break;
            }
        }else {
            view.toastMessage("无法连接到网络");
        }
    }

    @Override
    public void onSuccess(RefreshedToken refreshedToken) {
        PrefUtils.setLogin(true);
        PrefUtils.setToken("Bearer {" + refreshedToken.data + "}");
        interactor.getSchedule("Bearer {" + refreshedToken.data + "}", this);
    }

    @Override
    public void onFailure() {
        view.toastMessage("请重新登录");
        PrefUtils.setLogin(false);
        PrefUtils.setToken(null);
        view.startLoginActivity();
    }
}
