package com.twt.service.ui.news.details;

import com.twt.service.bean.News;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface OnGetNewsDetailsCallback {
    void onSuccess(News news);
    void onFailure(String errorMsg);
}
