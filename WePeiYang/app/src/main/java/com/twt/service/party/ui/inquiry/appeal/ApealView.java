package com.twt.service.party.ui.inquiry.appeal;

/**
 * Created by tjliqy on 2016/8/18.
 */
public interface ApealView {
    void appealSuccess(String msg);
    void appealFailure(String msg);
    void toastMsg(String msg);
}
