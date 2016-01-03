package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.login.OnLoginCallback;

/**
 * Created by sunjuntao on 16/1/1.
 */
public interface LoginInteractor {
    void login(String twtuname, String twtpasswd, OnLoginCallback onLoginCallback);
}
