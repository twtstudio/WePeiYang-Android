package com.twt.service.ui.date;

import android.util.Log;

import com.twt.service.bean.RestError;
import com.twt.service.interactor.TokenRefreshInteractor;
import com.twt.service.interactor.TokenRefreshInteractorImpl;
import com.twt.service.support.PrefUtils;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/5/3.
 */
public class DatingPresenterImpl implements DatingPresenter {

    private DatingView view;

    public DatingPresenterImpl(DatingView view) {
        this.view = view;
    }

    @Override
    public void tokenRefresh() {
        TokenRefreshInteractor interactor = new TokenRefreshInteractorImpl();
        interactor.refreshToken(PrefUtils.getToken());
    }

    @Override
    public void onTokenRefreshFailure(RetrofitError retrofitError) {
        switch (retrofitError.getKind()) {
            case HTTP:
                RestError error = (RestError) retrofitError.getBodyAs(RestError.class);
                if (error != null) {
                    switch (error.error_code) {
                        case 10000:
                        case 10001:
                        case 10002:
                        case 10004:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.removeToken();
                            view.startLoginActivity();
                            break;
                        case 10003:
                            Log.e("refresh", "refresh");
                            tokenRefresh();
                            break;
                        default:
                            view.toastMessage(error.message);
                            break;
                    }
                }
                break;
            case NETWORK:
                view.toastMessage("无法连接到网络");
                break;
            case CONVERSION:
            case UNEXPECTED:
                throw retrofitError;
            default:
                throw new AssertionError("未知的错误类型：" + retrofitError.getKind());
        }
    }
}
