package com.twt.service.interactor;

import com.twt.service.ui.feedback.OnFeedbackCallback;

/**
 * Created by sunjuntao on 16/1/8.
 */
public interface FeedbackInteractor {
    void feedback(String ua, String content, String email, OnFeedbackCallback onFeedbackCallback);
}
