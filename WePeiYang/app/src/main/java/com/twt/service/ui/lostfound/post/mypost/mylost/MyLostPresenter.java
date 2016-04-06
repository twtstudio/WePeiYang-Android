package com.twt.service.ui.lostfound.post.mypost.mylost;

import com.twt.service.bean.Lost;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/5.
 */
public interface MyLostPresenter {
    void refreshMyLostItems();

    void loadMoreMyLostItems();

    void onSuccess(Lost lost);

    void onFailure(RetrofitError error);

    void afterTokenRefreshed(String authorization);
}
