package com.rex.wepeiyang.ui.bind;

import com.google.gson.Gson;
import com.rex.wepeiyang.bean.RestError;
import com.rex.wepeiyang.interactor.BindInteractor;
import com.rex.wepeiyang.support.PrefUtils;
import com.rex.wepeiyang.ui.common.NextActivity;

import retrofit.RetrofitError;

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
        interactor.bind(PrefUtils.getToken(), tjuname, tjupwd, this);
    }

    @Override
    public void onSuccess() {
        view.hindProgress();
        view.toastMessage("绑定成功！");
        switch (nextActivity) {
            case Main:
                view.startMainActivity();
                break;
            case Gpa:
                view.startGpaActivity();
                break;
            case Schedule:
                view.startScheduleActivity();
        }
    }

    @Override
    public void onFailure(RetrofitError retrofitError) {
        view.hindProgress();
        RestError error = (RestError)retrofitError.getBodyAs(RestError.class);
        if (error != null) {
            switch (error.error_code) {
                case 20002:
                    view.toastMessage("用户名或密码错误");
                    break;
                case 10000:
                    view.toastMessage("请重新登录");
                    view.startLoginActivity();
                    break;
                default:
                    view.toastMessage(error.message);
            }
        } else {
            view.toastMessage("无法连接到网络");
        }
    }
}
