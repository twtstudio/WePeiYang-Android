package com.twt.service.ui.news.details;

import com.twt.service.bean.News;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface OnGetNewsDetailsCallback {
    void onSuccess(News news);
    void onFailure(RetrofitError error);
}
