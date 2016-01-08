package com.rex.wepeiyang.ui.feedback;

import com.rex.wepeiyang.bean.RestError;
import com.rex.wepeiyang.interactor.FeedbackInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/1/8.
 */
public class FeedbackPresenterImpl implements FeedbackPresenter, OnFeedbackCallback {

    private FeedbackView view;
    private FeedbackInteractor interactor;

    public FeedbackPresenterImpl(FeedbackView view, FeedbackInteractor interactor){
        this.view = view;
        this.interactor = interactor;
    }
    @Override
    public void feedback(String ua, String content, String email) {
        view.setSendClickable(false);
        interactor.feedback(ua, content, email, this);
    }

    @Override
    public void onSuccess() {
        view.setSendClickable(true);
        view.toastMessage("感谢您的反馈");
        view.finishActivity();
    }

    @Override
    public void onFailure(RetrofitError retrofitError) {
        view.setSendClickable(true);
        RestError error = (RestError)retrofitError.getBodyAs(RestError.class);
        if (error != null){
            view.toastMessage(error.message);
        }else {
            view.toastMessage("无法连接到网络");
        }
    }
}
