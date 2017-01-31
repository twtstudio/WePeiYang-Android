package com.twt.service.ui.bind;

/**
 * Created by sunjuntao on 16/1/1.
 */
public interface BindView {
    void showProgress();
    void hindProgress();
    void toastMessage(String msg);
    void startMainActivity();
    void startGpaActivity();
    void startLoginActivity();
    void startScheduleActivity();
    void startPartyActivity();
}
