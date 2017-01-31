package com.twt.service.ui.lostfound.found;

import com.twt.service.bean.Found;

/**
 * Created by sunjuntao on 16/2/22.
 */
public class SuccessEvent {
    public Found found;

    public SuccessEvent(Found found) {
        this.found = found;
    }

    public Found getFound() {
        return found;
    }
}
