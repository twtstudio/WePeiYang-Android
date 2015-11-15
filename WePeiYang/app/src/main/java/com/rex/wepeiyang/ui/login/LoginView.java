package com.rex.wepeiyang.ui.login;

/**
 * Created by sunjuntao on 15/11/15.
 */
public interface LoginView {
    void startMainActivity();
    void hideKeyboard();
    void usernameError(String errorMsg);
    void passwordError(String errorMsg);

}
