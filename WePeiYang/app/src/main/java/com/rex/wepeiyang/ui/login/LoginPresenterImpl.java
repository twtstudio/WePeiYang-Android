package com.rex.wepeiyang.ui.login;

import android.text.TextUtils;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.support.PrefUtils;
import com.rex.wepeiyang.support.ResourceHelper;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class LoginPresenterImpl implements LoginPresenter {
    private LoginView view;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
    }

    @Override
    public void validateLogin(String tjuuname, String tjupasswd) {
        view.hideKeyboard();
        if (TextUtils.isEmpty(tjuuname)) {
            view.usernameError(ResourceHelper.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(tjupasswd)) {
            view.passwordError(ResourceHelper.getString(R.string.empty_error));
            return;
        }
        PrefUtils.setLogin(true);
        PrefUtils.setTjuuname(tjuuname);
        PrefUtils.setTjuPasswd(tjupasswd);
        view.startMainActivity();
    }
}
