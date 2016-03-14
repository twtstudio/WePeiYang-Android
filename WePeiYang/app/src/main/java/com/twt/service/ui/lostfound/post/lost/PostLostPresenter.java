package com.twt.service.ui.lostfound.post.lost;

import retrofit.RetrofitError;

/**
 * Created by Rex on 2015/8/10.
 */
public interface PostLostPresenter {
    void postLost(String title, String name, String time, String place, String phone, String content, String lost_type);

    void onSuccess();

    void onFailure(RetrofitError error);
}
