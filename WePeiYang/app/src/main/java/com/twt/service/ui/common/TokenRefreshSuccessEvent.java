package com.twt.service.ui.common;

import com.twt.service.bean.RefreshedToken;

/**
 * Created by sunjuntao on 16/4/4.
 */
public class TokenRefreshSuccessEvent {
    private RefreshedToken refreshedToken;

    public TokenRefreshSuccessEvent(RefreshedToken refreshedToken) {
        this.refreshedToken = refreshedToken;
    }

    public RefreshedToken getRefreshedToken() {
        return refreshedToken;
    }
}
