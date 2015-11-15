package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.gpa.OnGetGpaCallback;

/**
 * Created by sunjuntao on 15/11/6.
 */
public interface GpaInteractor {
    void getGpa(String tjuuname, String tjupasswd, OnGetGpaCallback onGetGpaCallback);
}
