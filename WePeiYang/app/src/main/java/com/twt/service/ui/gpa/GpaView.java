package com.twt.service.ui.gpa;

import com.twt.service.bean.Gpa;
import com.twt.service.bean.GpaCaptcha;

/**
 * Created by sunjuntao on 15/11/15.
 */
public interface GpaView {
    void showProgress();

    void hideProgress();

    void toastMessage(String message);

    void bindData(Gpa gpa);

    void showCaptchaDialog(GpaCaptcha gpaCaptcha);
    void setClickable(boolean clickable);
    void startBindActivity();
    void startLoginActivity();
}
