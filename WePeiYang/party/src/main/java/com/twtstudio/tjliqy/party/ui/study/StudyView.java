package com.twtstudio.tjliqy.party.ui.study;

import com.twtstudio.tjliqy.party.bean.CourseInfo;
import com.twtstudio.tjliqy.party.bean.TextInfo;

import java.util.List;

/**
 * Created by tjliqy on 2016/8/17.
 */
public interface StudyView {
    void bindCourseData(List<CourseInfo> courseInfos);
    void bindTextData(List<TextInfo> textInfos);
    void onFailure();
    void toastMsg(String msg);
}
