package com.twt.service.ui.lostfound.post.lost;

import com.twt.service.interactor.LostInteractor;

/**
 * Created by Rex on 2015/8/10.
 */
public class PostLostPresenterImpl implements PostLostPresenter {

    private PostLostView view;
    private LostInteractor interactor;


    public PostLostPresenterImpl(PostLostView view, LostInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void postLost(int lost_type, String title, String time, String place, String content) {

    }
}
