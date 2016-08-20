package com.twt.service.party.interactor;

import com.twt.service.party.ui.study.OnGetCourseCallBack;
import com.twt.service.party.ui.study.detail.OnGetCourseDetailCallBack;
import com.twt.service.party.ui.study.detail.OnGetTextDetailCallBack;

/**
 * Created by tjliqy on 2016/8/17.
 */
public interface StudyInteractor {
    void getCourse(OnGetCourseCallBack callBack);
    void getCourseDetail(int course_id, OnGetCourseDetailCallBack callBack);
    void getText(OnGetCourseCallBack callBack);
    void getTextDetail(int textId, OnGetTextDetailCallBack callBack);
}
