package com.rex.wepeiyang.ui.schedule;

import com.rex.wepeiyang.bean.RefreshedToken;

/**
 * Created by sunjuntao on 16/1/7.
 */
public interface OnRefreshTokenCallback {
    void onSuccess(RefreshedToken refreshedToken);
    void onFailure();
}
