package com.twt.service.ui.lostfound.found.details;

import com.twt.service.bean.FoundDetails;

/**
 * Created by sunjuntao on 16/2/20.
 */
public class SuccessEvent {
    private FoundDetails details;

    public SuccessEvent(FoundDetails details) {
        this.details = details;
    }

    public FoundDetails getDetails() {
        return details;
    }
}
