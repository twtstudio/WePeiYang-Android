package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.gpa.OnGetGpaCallback;

/**
 * Created by sunjuntao on 15/11/6.
 */
public interface GpaInteractor {
    void getGpaWithoutToken(String tjuuname, String tjupasswd, OnGetGpaCallback onGetGpaCallback);
    void getGpaWithToken(String tjuuname, String tjupasswd, String token, String captcha, OnGetGpaCallback onGetGpaCallback);
}
