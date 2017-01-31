package com.twt.service.ui.notice;

import com.twt.service.bean.NewsList;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface OnGetNoticeCallback {
    void onSuccess(NewsList newsList);
    void onFailure(RetrofitError error);
}
