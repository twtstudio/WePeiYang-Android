package com.twtstudio.tjliqy.party.ui.study.answer;

/**
 * Created by tjliqy on 2016/8/26.
 */
public interface StudyResultView {
    void bindData(int status, int score, String msg);
    void setErrorMsg(String msg);
    void toastMsg(String msg);
}
