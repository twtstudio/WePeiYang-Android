package com.twt.service.ui.news.importantnews;

import com.twt.service.bean.NewsList;

/**
 * Created by sunjuntao on 15/11/14.
 */
public interface OnGetImportantNewsCallback {
    void onSuccess(NewsList newsList);
    void onFailure(String errorMsg);
}
