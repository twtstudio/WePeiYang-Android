package com.twt.service.ui.lostfound.post.lost;

import com.twt.service.bean.RestError;
import com.twt.service.interactor.LostInteractor;

import retrofit.RetrofitError;

/**
 * Created by Rex on 2015/8/10.
 */
public class PostLostPresenterImpl implements PostLostPresenter {

    private PostLostView view;
    private LostInteractor interactor;


    public PostLostPresenterImpl(PostLostView view, LostInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void postLost(String title, String name, String time, String place, String phone, String content, String lost_type) {
        view.showProgress();
        view.setSubmitClickable(false);
        interactor.postLost(title, name, time, place, phone, content, lost_type, "");
    }

    @Override
    public void onSuccess() {
        view.hideProgress();
        view.toastMessage("发布成功");
        view.finishActivity();
    }

    @Override
    public void onFailure(RetrofitError error) {
        view.hideProgress();
        switch (error.getKind()) {
            case HTTP:
                RestError restError = (RestError) error.getBodyAs(RestError.class);
                if (restError != null) {
                    view.toastMessage(restError.message);
                }
                break;
            case NETWORK:
                view.toastMessage("无法连接到网络");
                break;
            case CONVERSION:
            case UNEXPECTED:
                throw error;
            default:
                throw new AssertionError("未知的错误类型：" + error.getKind());
        }
    }
}
