package com.twt.service.interactor;

import com.twt.service.ui.notice.OnGetNoticeCallback;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface NoticeInteractor {
    void getNotice(int page, OnGetNoticeCallback onGetNoticeCallback);
}
