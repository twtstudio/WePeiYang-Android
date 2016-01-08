package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.feedback.OnFeedbackCallback;

/**
 * Created by sunjuntao on 16/1/8.
 */
public interface FeedbackInteractor {
    void feedback(String ua, String content, String email, OnFeedbackCallback onFeedbackCallback);
}
