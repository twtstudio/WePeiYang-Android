package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.RefreshedToken;
import com.twt.service.ui.common.TokenRefreshFailureEvent;
import com.twt.service.ui.common.TokenRefreshSuccessEvent;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 16/4/4.
 */
public class TokenRefreshInteractorImpl implements TokenRefreshInteractor {
    @Override
    public void refreshToken(String authorization) {
        ApiClient.refreshToken(authorization, new Callback<RefreshedToken>() {
            @Override
            public void success(RefreshedToken refreshedToken, Response response) {
                EventBus.getDefault().post(new TokenRefreshSuccessEvent(refreshedToken));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new TokenRefreshFailureEvent(error));
            }
        });
    }
}
