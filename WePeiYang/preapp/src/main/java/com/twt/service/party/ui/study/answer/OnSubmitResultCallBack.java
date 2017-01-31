package com.twt.service.party.ui.study.answer;

/**
 * Created by tjliqy on 2016/8/26.
 */
public interface OnSubmitResultCallBack {
    void onSubmitSuccess(int status, int score, String msg);
    void onSubmitError(String msg);
    void onSubmitFailure();
}
