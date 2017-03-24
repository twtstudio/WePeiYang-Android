package com.twtstudio.tjliqy.party.ui.study;

/**
 * Created by tjliqy on 2016/8/17.
 */
public interface StudyPresenter {
    void getCourse();
    void getCourseDetail(int courseId);
    void getText();
    void getTextDetail(int textId);
    void getQuiz(int courseId);
    void submitAnswer(int courseId, int[] rightAnswer, int[] exerciseAnswer);
}
