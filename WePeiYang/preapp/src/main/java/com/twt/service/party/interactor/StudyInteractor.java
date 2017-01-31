package com.twt.service.party.interactor;

import com.twt.service.party.ui.study.OnGetCourseCallBack;
import com.twt.service.party.ui.study.answer.OnGetQuizCallBack;
import com.twt.service.party.ui.study.answer.OnSubmitResultCallBack;
import com.twt.service.party.ui.study.detail.OnGetCourseDetailCallBack;
import com.twt.service.party.ui.study.detail.OnGetTextDetailCallBack;

/**
 * Created by tjliqy on 2016/8/17.
 */
public interface StudyInteractor {
    void getCourse(OnGetCourseCallBack callBack);
    void getCourseDetail(int coursId, OnGetCourseDetailCallBack callBack);
    void getText(OnGetCourseCallBack callBack);
    void getTextDetail(int textId, OnGetTextDetailCallBack callBack);
    void getQuizInfo(int courseId, OnGetQuizCallBack callBack);
    void submitAnswer(int courseId, String rightAnswer, String exerciseAnswer, OnSubmitResultCallBack callBack);
}
