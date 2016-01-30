package com.twt.service.ui.news.viewpoint;

import com.google.gson.Gson;
import com.twt.service.bean.NewsList;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.ViewPointInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/18.
 */
public class ViewPointPresenterImpl implements ViewPointPresenter, OnGetViewPointCallback {

    private ViewPointView view;
    private ViewPointInteractor interactor;
    private int page;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;

    public ViewPointPresenterImpl(ViewPointView view, ViewPointInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void refreshItems() {
        if (isRefreshing) {
            return;
        } else {
            page = 1;
            isRefreshing = true;
            interactor.getViewPoints(page, this);
        }
    }

    @Override
    public void loadMoreItems() {
        if (isLoadingMore){
            return;
        }else {
            page += 1;
            isLoadingMore = true;
            interactor.getViewPoints(page, this);
        }

    }

    @Override
    public void onSuccess(NewsList newsList) {
        view.setRefreshEnable(false);
        if (newsList.data.size() == 0) {
            isLoadingMore = false;
            isRefreshing = false;
            view.toastMessage("没有新闻了orz");
            return;
        }
        if (isRefreshing){
            isRefreshing = false;
            view.refreshItems(newsList.data);
        }else {
            isLoadingMore = false;
            view.loadMoreItems(newsList.data);
        }

    }

    @Override
    public void onFailure(RetrofitError error) {
        view.setRefreshEnable(false);
        isLoadingMore = false;
        isRefreshing = false;
        switch (error.getKind()){
            case HTTP:
                RestError restError = (RestError)error.getBodyAs(RestError.class);
                if (restError!= null){
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
