package com.twt.service.interactor;

import com.twt.service.ui.gpa.OnGetGpaCallback;
import com.twt.service.ui.gpa.OnRefreshTokenCallback;

/**
 * Created by sunjuntao on 15/11/6.
 */
public interface GpaInteractor {
    void getGpaWithoutToken(String authorization, OnGetGpaCallback onGetGpaCallback);
    void getGpaWithToken(String authorization, String token, String captcha, OnGetGpaCallback onGetGpaCallback);
    void refreshToken(String authorization, OnRefreshTokenCallback onRefreshTokenCallback);
}
