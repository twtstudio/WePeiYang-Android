package com.rex.wepeiyang.ui.gpa;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.bean.GpaCaptcha;
import com.rex.wepeiyang.interactor.GpaInteractor;
import com.rex.wepeiyang.support.PrefUtils;

import java.io.StringReader;

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
    public void getGpaWithoutToken() {
        view.showProgress();
        String tjuuname = PrefUtils.getTjuUname();
        String tjupasswd = PrefUtils.getTjuPasswd();
        interactor.getGpaWithoutToken(tjuuname, tjupasswd, this);
    }

    @Override
    public void getGpaWithToken(String token, String captcha) {
        view.showProgress();
        String tjuuname = PrefUtils.getTjuUname();
        String tjupasswd = PrefUtils.getTjuPasswd();
        interactor.getGpaWithToken(tjuuname, tjupasswd, token, captcha, this);
    }

    @Override
    public void onSuccess(JsonElement jsonElement) {
        view.hideProgress();
        String json = jsonElement.toString();
        Log.e("json", json);
        if (json.contains("token")) {
            GpaCaptcha gpaCaptcha = new Gson().fromJson(json, GpaCaptcha.class);
            view.showCaptchaDialog(gpaCaptcha);
        } else {
            Gpa gpa = new Gson().fromJson(json, Gpa.class);
            view.bindData(gpa);
        }
    }

    @Override
    public void onFailure(String errorMsg) {
        view.hideProgress();
        view.toastMessage(errorMsg);
    }
}
