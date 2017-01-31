package com.twt.service.ui.gpa.evalution;

/**
 * Created by tjliqy on 2017/1/15.
 */

public interface EvaluatePresenter {
    void postEvaluate(String lessonId, String unionId, String courseId, String term, int star);
    void postEvaluate(String lessonId, String unionId, String courseId, String term, int[] fiveQ, String note);
}
