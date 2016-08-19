package com.twt.service.bike.common;

/**
 * Created by huangyong on 16/5/19.
 */
public interface IViewController {
    void toastMessage(String message);

    void showLoadingDialog();
    void showLoadingDialog(String message);
    void dismissLoadingDialog();
}
