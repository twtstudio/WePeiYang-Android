package com.twt.service.ui.lostfound.found.details;

import com.twt.service.bean.FoundDetails;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.FoundInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/20.
 */
public class FoundDetailsPresenterImpl implements FoundDetailsPresenter, OnGetFoundDetailsCallback {
    private FoundDetailsView view;
    private FoundInteractor interactor;

    public FoundDetailsPresenterImpl(FoundDetailsView view, FoundInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void getFoundDetails(int id) {
        view.showProgress();
        interactor.getFoundDetails(id);
    }

    @Override
    public void onSuccess(FoundDetails details) {
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
