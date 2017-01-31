package com.twt.service.ui.lostfound.post.mypost.mylost.event;

import com.twt.service.bean.Lost;

/**
 * Created by sunjuntao on 16/4/6.
 */
public class GetMyLostSuccessEvent {
    private Lost lost;

    public GetMyLostSuccessEvent(Lost lost) {
        this.lost = lost;
    }

    public Lost getLost() {
        return lost;
    }
}
