package com.rex.wepeiyang.ui.gpa;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rex.wepeiyang.bean.*;
import com.rex.wepeiyang.bean.Error;
import com.rex.wepeiyang.interactor.GpaInteractor;
import com.rex.wepeiyang.support.PrefUtils;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/22.
 */
public class GpaPresenterImpl implements GpaPresenter, OnGetGpaCallback, OnRefreshTokenCallback {

    private GpaView view;
    private GpaInteractor interactor;

    public GpaPresenterImpl(GpaView view, GpaInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void getGpaWithoutToken() {
        view.showProgress();
        interactor.getGpaWithoutToken(PrefUtils.getToken(), this);
    }

    @Override
    public void getGpaWithToken(String token, String captcha) {
        view.showProgress();
        interactor.getGpaWithToken(PrefUtils.getToken(), token, captcha, this);
    }

    @Override
    public void onSuccess(JsonElement jsonElement) {
        view.hideProgress();
        String json = jsonElement.toString();
        if (json.contains("token")) {
            GpaCaptcha gpaCaptcha = new Gson().fromJson(json, GpaCaptcha.class);
            view.showCaptchaDialog(gpaCaptcha);
        } else {
            view.setClickable(true);
            Gpa gpa = new Gson().fromJson(json, Gpa.class);
            view.bindData(gpa);
        }
    }

    @Override
    public void onFailure(RetrofitError retrofitError) {
        com.rex.wepeiyang.bean.Error error = new Gson().fromJson(retrofitError.getBody().toString(), Error.class);
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
        }
    }

    @Override
    public void onSuccess(RefreshedToken refreshedToken) {
        PrefUtils.setLogin(true);
        PrefUtils.setToken(refreshedToken.data);
        interactor.getGpaWithoutToken(refreshedToken.data, this);
    }

    @Override
    public void onFailure(String errorMsg) {
        view.toastMessage("请重新登录");
        PrefUtils.setLogin(false);
        PrefUtils.setToken(null);
        view.startLoginActivity();
    }
}
