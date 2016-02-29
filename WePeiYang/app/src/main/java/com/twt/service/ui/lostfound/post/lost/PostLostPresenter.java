package com.twt.service.ui.lostfound.post.lost;

/**
 * Created by Rex on 2015/8/10.
 */
public interface PostLostPresenter {
    void postLost(int lost_type, String title, String time, String place, String content);
}
