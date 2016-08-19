package com.twt.service.party.ui.study;

import com.twt.service.party.bean.CourseDetailInfo;
import com.twt.service.party.bean.CourseInfo;
import com.twt.service.party.bean.TextInfo;
import com.twt.service.party.interactor.StudyInteractor;
import com.twt.service.party.ui.study.detail.OnGetCourseDetailCallBack;
import com.twt.service.party.ui.study.detail.StudyDetailActivity;
import com.twt.service.party.ui.study.detail.StudyDetailListView;
import com.twt.service.party.ui.study.detail.StudyDetailView;

import java.util.List;

/**
 * Created by tjliqy on 2016/8/17.
 */
public class StudyPresenterImpl implements StudyPresenter, OnGetCourseCallBack, OnGetCourseDetailCallBack{

    private StudyInteractor interactor;
    private StudyView view;
    private StudyDetailListView detailListView;
    private StudyDetailView detailView;
    public StudyPresenterImpl(StudyView view, StudyInteractor interactor) {
        this.interactor = interactor;
        this.view = view;
    }

    public StudyPresenterImpl(StudyDetailListView view,StudyInteractor interactor){
        this.interactor = interactor;
        detailListView = view;
    }

    public StudyPresenterImpl(StudyDetailView view,StudyInteractor interactor){
        this.interactor = interactor;
        detailView = view;
    }
    @Override
    public void getCourse() {
        interactor.getCourse(this);
    }

    @Override
    public void getText() {
        interactor.getText(this);
    }

    @Override
    public void onGetCourseInfo(List<CourseInfo> courseInfos) {
        view.bindCourseData(courseInfos);
    }

    @Override
    public void onGetTextInfo(List<TextInfo> textInfos) {
        view.bindTextData(textInfos);
    }

    @Override
    public void getCourseDetail(int courseId) {
        interactor.getCourseDetail(courseId,this);
    }

    @Override
    public void getTextDetail() {

    }

    @Override
    public void onGetDetailSuccess(List<CourseDetailInfo.DataBean> list) {
        detailListView.onGetCourseDetail(list);
    }
}
