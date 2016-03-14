package com.twt.service.ui.lostfound.post.found;

/**
 * Created by sunjuntao on 16/3/14.
 */
public interface PostFoundView {
    void showProgress();

    void hideProgress();

    void toastMessage(String msg);

    void finishActivity();
}
