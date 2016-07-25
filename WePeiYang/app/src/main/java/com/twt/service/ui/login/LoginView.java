package com.twt.service.ui.login;

/**
 * Created by sunjuntao on 15/11/15.
 */
public interface LoginView {
    void startMainActivity();

    void startGpaActivity();

    void startScheduleActivity();

    void startPostLostFoundActivity();

    void startMyLostFoundActivity();

    void startDatingActivity();

    void startBusActivity();

    void startPartyActivity();

    void hideKeyboard();

    void usernameError(String errorMsg);

    void passwordError(String errorMsg);

    void showToast(String msg);

    void showProcessing();

    void hideProcessing();

}
