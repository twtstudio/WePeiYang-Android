package com.twt.service.ui.lostfound.post.found;

import com.twt.service.bean.FoundDetails;

/**
 * Created by sunjuntao on 16/3/14.
 */
public interface PostFoundView {
    void showProgress();

    void hideProgress();

    void toastMessage(String msg);

    void bindData(FoundDetails foundDetails);

    void finishActivity();

    void startLoginActivity();

    void setSubmitClickable(boolean clickable);
}
