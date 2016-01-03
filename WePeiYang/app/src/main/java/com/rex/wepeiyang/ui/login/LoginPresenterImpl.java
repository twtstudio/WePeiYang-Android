package com.rex.wepeiyang.ui.login;

import android.text.TextUtils;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.Login;
import com.rex.wepeiyang.interactor.LoginInteractor;
import com.rex.wepeiyang.support.PrefUtils;
import com.rex.wepeiyang.support.ResourceHelper;
import com.rex.wepeiyang.ui.common.NextActivity;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class LoginPresenterImpl implements LoginPresenter, OnLoginCallback {
    private LoginView view;
    private LoginInteractor interactor;
    private NextActivity nextActivity;
    private String twtuname;


    public LoginPresenterImpl(LoginView view, LoginInteractor interactor, NextActivity nextActivity) {
        this.view = view;
        this.interactor = interactor;
        this.nextActivity = nextActivity;
    }

    @Override
    public void validateLogin(String twtuname, String twtpasswd) {
        view.showProcessing();
        view.hideKeyboard();
        if (TextUtils.isEmpty(twtuname)) {
            view.usernameError(ResourceHelper.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(twtpasswd)) {
            view.passwordError(ResourceHelper.getString(R.string.empty_error));
            return;
        }
        interactor.login(twtuname, twtpasswd, this);
        this.twtuname = twtuname;
    }

    @Override
    public void onSuccess(Login login) {
        view.hideProcessing();
        PrefUtils.setToken(login.data.token);
        PrefUtils.setLogin(true);
        if (twtuname != null) {
            PrefUtils.setUsername(twtuname);
        }
        switch (nextActivity) {
            case Main:
                view.startMainActivity();
                break;
            case Gpa:
                view.startGpaActivity();
                break;
            case Schedule:
                view.startScheduleActivity();
                break;
        }
    }

    @Override
    public void onFailure(String errorMsg) {
        view.hideProcessing();
        view.showToast(errorMsg);
    }
}
