package com.twt.service.ui.news.comments;

import com.twt.service.bean.CommentCallback;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/14.
 */
public interface OnPostNewsCommentsCallback {
    void onSuccess(CommentCallback callback);

    void onFailure(RetrofitError error);
}
