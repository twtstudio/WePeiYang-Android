package com.twt.service.ui.news.importantnews;

import com.twt.service.bean.NewsList;
import com.twt.service.bean.RestError;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/14.
 */
public interface OnGetImportantNewsCallback {
    void onSuccess(NewsList newsList);
    void onFailure(RetrofitError error);
}
