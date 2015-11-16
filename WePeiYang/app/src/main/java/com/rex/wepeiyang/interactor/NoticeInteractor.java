package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.notice.OnGetNoticeCallback;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface NoticeInteractor {
    void getNotice(int page, OnGetNoticeCallback onGetNoticeCallback);
}
