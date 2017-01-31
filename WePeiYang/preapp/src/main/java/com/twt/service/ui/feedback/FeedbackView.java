package com.twt.service.ui.feedback;

/**
 * Created by sunjuntao on 16/1/8.
 */
public interface FeedbackView {
    void toastMessage(String msg);
    void setSendClickable(boolean clickable);
    void finishActivity();
}
