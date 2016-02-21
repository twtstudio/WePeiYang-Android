package com.twt.service.ui.lostfound.lost.details;

import com.twt.service.bean.LostDetails;

/**
 * Created by sunjuntao on 16/2/19.
 */
public class SuccessEvent {
    private LostDetails lostDetails;

    public SuccessEvent(LostDetails lostDetails) {
        this.lostDetails = lostDetails;
    }

    public LostDetails getLostDetails() {
        return lostDetails;
    }
}
