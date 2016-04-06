package com.twt.service.ui.lostfound.post.lost;

/**
 * Created by Rex on 2015/8/10.
 */
public interface PostLostView {
    void toastMessage(String message);

    void showProgress();

    void hideProgress();

    void setSubmitClickable(boolean clickable);

    void finishActivity();

    void startLoginActivity();
}
