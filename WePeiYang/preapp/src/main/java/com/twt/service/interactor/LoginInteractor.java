package com.twt.service.interactor;

import com.twt.service.ui.login.OnLoginCallback;

/**
 * Created by sunjuntao on 16/1/1.
 */
public interface LoginInteractor {
    void login(String twtuname, String twtpasswd, OnLoginCallback onLoginCallback);
}
