package com.twt.service.party.ui.study.answer;

/**
 * Created by tjliqy on 2016/8/25.
 */
public interface StudyAnswerBridge {
    void setAnswer(int position, int pos);
    void cancelAnswer(int positon, int pos);
    void setSingleAnswer(int position, int pos);
    void click(int position);
}
