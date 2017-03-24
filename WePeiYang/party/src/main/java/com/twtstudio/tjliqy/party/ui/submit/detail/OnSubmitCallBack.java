package com.twtstudio.tjliqy.party.ui.submit.detail;

/**
 * Created by tjliqy on 2016/8/22.
 */
public interface OnSubmitCallBack {
    void onSuccess(String msg);
    void onError(String msg);
    void onFailure();
}
