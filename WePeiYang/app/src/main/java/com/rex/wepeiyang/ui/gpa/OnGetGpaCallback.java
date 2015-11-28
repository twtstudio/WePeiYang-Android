package com.rex.wepeiyang.ui.gpa;

import com.google.gson.JsonElement;

/**
 * Created by sunjuntao on 15/11/15.
 */
public interface OnGetGpaCallback {
    void onSuccess(JsonElement jsonElement);
    void onFailure(String errorMsg);
}
