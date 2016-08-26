package com.twt.service.party.ui.study;

import com.twt.service.R;
import com.twt.service.party.bean.CourseDetailInfo;
import com.twt.service.party.bean.CourseInfo;
import com.twt.service.party.bean.QuizInfo;
import com.twt.service.party.bean.TextDetailInfo;
import com.twt.service.party.bean.TextInfo;
import com.twt.service.party.interactor.StudyInteractor;
import com.twt.service.party.ui.study.answer.OnGetQuizCallBack;
import com.twt.service.party.ui.study.answer.OnSubmitResultCallBack;
import com.twt.service.party.ui.study.answer.StudyAnswerView;
import com.twt.service.party.ui.study.answer.StudyResultView;
import com.twt.service.party.ui.study.detail.OnGetCourseDetailCallBack;
import com.twt.service.party.ui.study.detail.OnGetTextDetailCallBack;
import com.twt.service.party.ui.study.detail.StudyDetailActivity;
import com.twt.service.party.ui.study.detail.StudyDetailListView;
import com.twt.service.party.ui.study.detail.StudyDetailView;
import com.twt.service.support.ResourceHelper;

import java.util.List;

/**
 * Created by tjliqy on 2016/8/17.
 */
public class StudyPresenterImpl implements StudyPresenter, OnGetCourseCallBack, OnGetCourseDetailCallBack, OnGetTextDetailCallBack, OnGetQuizCallBack, OnSubmitResultCallBack {

    private StudyInteractor interactor;
    private StudyView view;
    private StudyDetailListView detailListView;
    private StudyDetailView detailView;
    private StudyAnswerView answerView;
    private StudyResultView resultView;

    public StudyPresenterImpl(StudyView view, StudyInteractor interactor) {
        this.interactor = interactor;
        this.view = view;
    }

    public StudyPresenterImpl(StudyDetailListView view, StudyInteractor interactor) {
        this.interactor = interactor;
        detailListView = view;
    }

    public StudyPresenterImpl(StudyDetailView view, StudyInteractor interactor) {
        this.interactor = interactor;
        detailView = view;
    }

    public StudyPresenterImpl(StudyResultView view, StudyInteractor interactor) {
        this.interactor = interactor;
        resultView = view;
    }

    public StudyPresenterImpl(StudyAnswerView view, StudyInteractor interactor) {
        this.interactor = interactor;
        answerView = view;
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
    public void onGetFailure() {
        view.onFailure();
    }

    @Override
    public void getCourseDetail(int courseId) {
        interactor.getCourseDetail(courseId, this);
    }

    @Override
    public void getTextDetail(int textId) {
        interactor.getTextDetail(textId, this);
    }

    @Override
    public void getQuiz(int courseId) {
        interactor.getQuizInfo(courseId, this);
    }

    @Override
    public void submitAnswer(int courseId, int[] rightAnswer, int[] exerciseAnswer) {
        interactor.submitAnswer(courseId, rightAnswer, exerciseAnswer, this);
    }

    @Override
    public void onGetDetailSuccess(List<CourseDetailInfo.DataBean> list) {
        detailListView.onGetCourseDetail(list);
    }

    @Override
    public void onGetDetailSuccess(TextDetailInfo detailInfo) {
        detailView.onGetTextDetail(detailInfo);
    }

    @Override
    public void onGetQuizSuccess(List<QuizInfo.DataBean> dataList) {
        answerView.bindData(dataList);
    }

    @Override
    public void onGetQuizError(String msg) {
        answerView.setError(msg);
    }

    @Override
    public void onGetQuizFailure() {
        answerView.toastMsg(ResourceHelper.getString(R.string.toast_network_failed));
    }

    @Override
    public void onSubmitSuccess(int status, int score, String msg) {
        resultView.bindData(status, score, msg);
    }

    @Override
    public void onSubmitError(String msg) {
        resultView.setErrorMsg(msg);
    }

    @Override
    public void onSubmitFailure() {
        resultView.setErrorMsg(ResourceHelper.getString(R.string.toast_network_failed));
    }
}
