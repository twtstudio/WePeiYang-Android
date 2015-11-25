package com.rex.wepeiyang.ui.gpa;

import com.rex.wepeiyang.bean.Gpa;

/**
 * Created by sunjuntao on 15/11/15.
 */
public interface OnGetGpaCallback {
    void onSuccess(Object response);
    void onFailure(String errorMsg);
}
