package com.twt.service.ui.lostfound.lost;

import com.twt.service.bean.Lost;

/**
 * Created by sunjuntao on 16/2/22.
 */
public class SuccessEvent {
    private Lost lost;

    public SuccessEvent(Lost lost) {
        this.lost = lost;
    }

    public Lost getLost() {
        return lost;
    }
}
