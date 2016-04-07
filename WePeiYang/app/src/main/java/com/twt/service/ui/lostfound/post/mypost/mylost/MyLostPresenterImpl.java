package com.twt.service.ui.lostfound.post.mypost.mylost;

import android.util.Log;

import com.twt.service.bean.Lost;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.LostInteractor;
import com.twt.service.interactor.TokenRefreshInteractor;
import com.twt.service.interactor.TokenRefreshInteractorImpl;
import com.twt.service.support.PrefUtils;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/4/5.
 */
public class MyLostPresenterImpl implements MyLostPresenter {

    private MyLostView view;
    private LostInteractor interactor;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    private int page;

    public MyLostPresenterImpl(MyLostView view, LostInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void refreshMyLostItems() {
        if (isRefreshing) {
            return;
        } else {
            isRefreshing = true;
            page = 1;
            interactor.getMyLostList(PrefUtils.getToken(), page);
        }
    }

    @Override
    public void loadMoreMyLostItems() {
        if (isLoadingMore) {
            return;
        } else {
            isLoadingMore = true;
            page += 1;
            interactor.getMyLostList(PrefUtils.getToken(), page);
        }
    }

    @Override
    public void onSuccess(Lost lost) {
        if (isRefreshing) {
            isRefreshing = false;
            view.hideRefreshing();
            view.refreshItems(lost.data);
        }
        if (isLoadingMore) {
            isLoadingMore = false;
            view.loadMoreItems(lost.data);
        }
    }

    @Override
    public void onFailure(RetrofitError error) {
        isRefreshing = false;
        isLoadingMore = false;
        view.hideRefreshing();
        switch (error.getKind()) {
            case HTTP:
                RestError restError = (RestError) error.getBodyAs(RestError.class);
                if (restError != null) {
                    switch (restError.error_code) {
                        case 10000:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.removeToken();
                            view.startLoginActivity();
                            break;
                        case 10001:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.removeToken();
                            view.startLoginActivity();
                            break;
                        case 10002:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.removeToken();
                            view.startLoginActivity();
                            break;
                        case 10003:
                            Log.e("refresh", "refresh");
                            TokenRefreshInteractor tokenRefreshInteractor = new TokenRefreshInteractorImpl();
                            tokenRefreshInteractor.refreshToken(PrefUtils.getToken());
                            break;
                        default:
                            view.toastMessage(restError.message);
                            break;
                    }
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

    @Override
    public void afterTokenRefreshed(String authorization) {
        interactor.getMyLostList(authorization, page);
    }
}
