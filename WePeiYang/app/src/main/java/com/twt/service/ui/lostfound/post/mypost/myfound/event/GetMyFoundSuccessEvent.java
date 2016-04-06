package com.twt.service.ui.lostfound.post.mypost.myfound.event;

import com.twt.service.bean.Found;

/**
 * Created by sunjuntao on 16/4/6.
 */
public class GetMyFoundSuccessEvent {
    private Found found;

    public GetMyFoundSuccessEvent(Found found) {
        this.found = found;
    }

    public Found getFound() {
        return found;
    }
}
