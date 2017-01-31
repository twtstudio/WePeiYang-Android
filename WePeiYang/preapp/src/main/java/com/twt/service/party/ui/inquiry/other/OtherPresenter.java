package com.twt.service.party.ui.inquiry.other;

/**
 * Created by tjliqy on 2016/8/16.
 */
public interface OtherPresenter {
    void getGrade(String type);
    void appeal(String title, String content, String type, int testId);
}
