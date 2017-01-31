package com.twt.service.ui.gpa;

import com.twt.service.bean.RefreshedToken;


/**
 * Created by sunjuntao on 16/1/7.
 */
public interface OnRefreshTokenCallback {
    void onSuccess(RefreshedToken refreshedToken);
    void onFailure();
}
