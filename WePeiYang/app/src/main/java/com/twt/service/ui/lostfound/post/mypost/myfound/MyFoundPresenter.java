package com.twt.service.ui.lostfound.post.mypost.myfound;

import com.twt.service.bean.Found;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/5.
 */
public interface MyFoundPresenter {
    void refreshMyFoundItems();

    void loadMoreMyFoundItems();

    void onSuccess(Found found);

    void onFailure(RetrofitError error);

    void afterTokenRefreshed(String authorization);
}
