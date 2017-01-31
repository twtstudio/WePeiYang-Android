package com.twt.service.ui.news.comments;

import com.twt.service.bean.CommentCallback;

/**
 * Created by sunjuntao on 16/2/14.
 */
public class SuccessEvent {
    private CommentCallback callback;

    public SuccessEvent(CommentCallback callback) {
        this.callback = callback;
    }

    public CommentCallback getCallback() {
        return callback;
    }
}
