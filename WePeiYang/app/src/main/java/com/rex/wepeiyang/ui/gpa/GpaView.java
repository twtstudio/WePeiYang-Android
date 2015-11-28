package com.rex.wepeiyang.ui.gpa;

import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.bean.GpaCaptcha;

/**
 * Created by sunjuntao on 15/11/15.
 */
public interface GpaView {
    void initTab();
    void showProgress();
    void hideProgress();
    void toastMessage(String message);
    void bindData(Gpa gpa);
    void showCaptchaDialog(GpaCaptcha gpaCaptcha);
}
