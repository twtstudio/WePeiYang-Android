package com.twt.service.ui.auth;

import com.twt.service.common.IViewController;

/**
 * Created by huangyong on 16/5/18.
 */
public interface AuthViewController extends IViewController {
    void showUsernameError(String message);
    void showPasswordError(String message);
}
