package com.twt.service.ui.notice;

import com.twt.service.bean.NewsList;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface OnGetNoticeCallback {
    void onSuccess(NewsList newsList);
    void onFailure(String errorMsg);
}
