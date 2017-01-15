package com.twt.service.ui.gpa.evalution;

import android.content.Context;

import com.twt.service.support.PrefUtils;

import retrofit.RetrofitError;

/**
 * Created by tjliqy on 2017/1/15.
 */

public class EvaluatePresenterImpl implements EvaluatePresenter,onPostEvaluateCallBack {

    EvaluateView mView;
    EvaluateIntercepter mIntercepter;
    Context mContext;

    public EvaluatePresenterImpl(EvaluateView view, EvaluateIntercepter intercepter, Context context) {
        mView = view;
        mIntercepter = intercepter;
        mContext = context;
    }

    @Override
    public void postEvaluate(String lessonId, String unionId, String courseId, String term, int[] fiveQ, String note){
        mIntercepter.postGpaEvaluate(PrefUtils.getToken(),PrefUtils.getGpaToken(),lessonId,unionId,courseId,term,fiveQ,note,this);
    }

    @Override
    public void postEvaluate(String lessonId, String unionId, String courseId, String term, int star) {
        int[] fiveQ = {star,star,star,star,star};
        mIntercepter.postGpaEvaluate(PrefUtils.getToken(),PrefUtils.getGpaToken(),lessonId,unionId,courseId,term,fiveQ,"",this);
    }

    @Override
    public void onSuccess(String message) {
        mView.toastMessage("评价成功！");
        mView.deleteCourse();
    }

    @Override
    public void onFailure(RetrofitError error) {
        mView.toastMessage(error.getMessage());
    }
}
