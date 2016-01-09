package com.twt.service.ui.main;

import com.twt.service.bean.Main;

/**
 * Created by sunjuntao on 15/12/3.
 */
public interface OnGetMainCallback {
    void onSuccess(Main main);
    void onFailure(String errorMsg);
}
