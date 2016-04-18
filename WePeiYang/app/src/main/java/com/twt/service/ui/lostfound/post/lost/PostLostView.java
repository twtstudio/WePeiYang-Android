package com.twt.service.ui.lostfound.post.lost;

import com.twt.service.bean.LostDetails;

/**
 * Created by Rex on 2015/8/10.
 */
public interface PostLostView {
    void toastMessage(String message);

    void showProgress();

    void hideProgress();

    void setSubmitClickable(boolean clickable);

    void setChangeClickable(boolean clickable);

    void finishActivity();

    void startLoginActivity();

    void bindData(LostDetails lostDetails);
}
