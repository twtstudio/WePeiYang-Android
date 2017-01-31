package com.twt.service.ui.news.viewpoint;

import com.twt.service.bean.NewsList;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/18.
 */
public interface OnGetViewPointCallback {
    void onSuccess(NewsList newsList);

    void onFailure(RetrofitError error);
}
