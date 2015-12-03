package com.rex.wepeiyang.ui.main;

import com.rex.wepeiyang.bean.Main;

/**
 * Created by sunjuntao on 15/12/3.
 */
public interface OnGetMainCallback {
    void onSuccess(Main main);
    void onFailure(String errorMsg);
}
