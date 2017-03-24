package com.twtstudio.tjliqy.party.ui.study.detail;

import com.twtstudio.tjliqy.party.bean.CourseDetailInfo;

import java.util.List;

/**
 * Created by tjliqy on 2016/8/18.
 */
public interface StudyDetailListView {
    void onGetCourseDetail(List<CourseDetailInfo.DataBean> list);
}
