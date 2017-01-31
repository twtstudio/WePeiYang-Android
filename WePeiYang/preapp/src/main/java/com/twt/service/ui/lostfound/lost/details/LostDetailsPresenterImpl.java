package com.twt.service.ui.lostfound.lost.details;

import com.twt.service.bean.LostDetails;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.LostInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/20.
 */
public class LostDetailsPresenterImpl implements LostDetailsPresenter, OnGetLostDetailsCallback {

    private LostDetailsView view;
    private LostInteractor interactor;

    public LostDetailsPresenterImpl(LostDetailsView view, LostInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void getLostDetails(int id) {
        view.showProgress();
        interactor.getLostDetails(id);
    }

    @Override
    public void onSuccess(LostDetails details) {
        view.hideProgress();
        view.bindData(details);
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
