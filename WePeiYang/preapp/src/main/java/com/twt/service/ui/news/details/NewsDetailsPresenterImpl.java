package com.twt.service.ui.news.details;

import com.twt.service.bean.News;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.NewsDetailsInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/16.
 */
public class NewsDetailsPresenterImpl implements NewsDetailsPresenter, OnGetNewsDetailsCallback {

    private NewsDetailsView view;
    private NewsDetailsInteractor interactor;

    public NewsDetailsPresenterImpl(NewsDetailsView view, NewsDetailsInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void loadNewsDetails(int index) {
        view.showProgress();
        interactor.getNewsDetails(index, this);
    }

    @Override
    public void onSuccess(News news) {
        view.hideProgress();
        view.bindData(news);
    }

    @Override
    public void onFailure(RetrofitError error) {
        view.hideProgress();
        switch (error.getKind()) {
            case HTTP:
                // TODO get message from getResponse()'s body or HTTP status
                RestError restError = (RestError)error.getBodyAs(RestError.class);
                if (restError!=null){
                    view.toastMessage(restError.message);
                }
                break;

            case NETWORK:
                // TODO get message from getCause()'s message or just declare "network problem"
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
