package com.twt.service.party.interactor;

import android.util.Log;

import com.twt.service.api.Api;
import com.twt.service.party.api.ApiClient;
import com.twt.service.party.bean.CourseDetailInfo;
import com.twt.service.party.bean.Status;
import com.twt.service.party.bean.TextDetailInfo;
import com.twt.service.party.ui.study.OnGetCourseCallBack;
import com.twt.service.party.ui.study.detail.OnGetCourseDetailCallBack;
import com.twt.service.party.ui.study.detail.OnGetTextDetailCallBack;
import com.twt.service.support.PrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tjliqy on 2016/8/17.
 */
public class StudyInteractorImpl implements StudyInteractor {
    @Override
    public void getCourse(final OnGetCourseCallBack callBack) {
        ApiClient.loadStatus("applicant_coursestudy", PrefUtils.getPrefUserNumber(), new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                callBack.onGetCourseInfo(response.body().getCourselist());
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }

    @Override
    public void getText(final OnGetCourseCallBack callBack) {
        ApiClient.loadStatus("study_text", null, new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                callBack.onGetTextInfo(response.body().getTextlist());
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }

    @Override
    public void getCourseDetail(int courseId, final OnGetCourseDetailCallBack callBack) {
        ApiClient.loadCourseDetail(PrefUtils.getPrefUserNumber(), courseId, new Callback<CourseDetailInfo>() {
            @Override
            public void onResponse(Call<CourseDetailInfo> call, Response<CourseDetailInfo> response) {
                callBack.onGetDetailSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<CourseDetailInfo> call, Throwable t) {

            }
        });
    }

    @Override
    public void getTextDetail(int textId, final OnGetTextDetailCallBack callBack) {
        ApiClient.loadTextDetail(PrefUtils.getPrefUserNumber(), textId, new Callback<TextDetailInfo>() {
            @Override
            public void onResponse(Call<TextDetailInfo> call, Response<TextDetailInfo> response) {
                Log.d("lqy",response.body().toString());
                callBack.onGetDetailSuccess(response.body());
            }

            @Override
            public void onFailure(Call<TextDetailInfo> call, Throwable t) {

            }
        });
    }
}
