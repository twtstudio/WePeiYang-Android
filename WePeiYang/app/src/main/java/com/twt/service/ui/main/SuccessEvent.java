package com.twt.service.ui.main;

/**
 * Created by sunjuntao on 16/1/27.
 */
public class SuccessEvent {
    private String successString;

    public SuccessEvent(String successString) {
        this.successString = successString;
    }

    @Override
    public String toString() {
        return successString;
    }
}
