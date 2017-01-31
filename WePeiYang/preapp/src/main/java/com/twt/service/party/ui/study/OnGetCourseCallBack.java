package com.twt.service.party.ui.study;

import com.twt.service.party.bean.CourseInfo;
import com.twt.service.party.bean.TextInfo;

import java.util.List;

/**
 * Created by tjliqy on 2016/8/17.
 */
public interface OnGetCourseCallBack {
    void onGetCourseInfo(List<CourseInfo> courseInfos);
    void onGetTextInfo(List<TextInfo> textInfos);
    void onGetFailure();
}
