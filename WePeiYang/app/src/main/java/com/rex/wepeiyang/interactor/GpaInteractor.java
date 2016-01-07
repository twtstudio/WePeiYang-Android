package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.gpa.OnGetGpaCallback;
import com.rex.wepeiyang.ui.gpa.OnRefreshTokenCallback;

/**
 * Created by sunjuntao on 15/11/6.
 */
public interface GpaInteractor {
    void getGpaWithoutToken(String authorization, OnGetGpaCallback onGetGpaCallback);
    void getGpaWithToken(String authorization, String token, String captcha, OnGetGpaCallback onGetGpaCallback);
    void refreshToken(String authorization, OnRefreshTokenCallback onRefreshTokenCallback);
}
