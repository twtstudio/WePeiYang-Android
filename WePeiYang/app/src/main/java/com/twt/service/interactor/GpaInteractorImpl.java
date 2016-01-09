package com.twt.service.interactor;


import com.google.gson.JsonElement;
import com.twt.service.api.ApiClient;
import com.twt.service.bean.RefreshedToken;
import com.twt.service.ui.gpa.OnGetGpaCallback;
import com.twt.service.ui.gpa.OnRefreshTokenCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/11/6.
 */
public class GpaInteractorImpl implements GpaInteractor {
    @Override
    public void getGpaWithoutToken(String authorization, final OnGetGpaCallback onGetGpaCallback) {
        ApiClient.getGpaWithoutToken(authorization, new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {
                onGetGpaCallback.onSuccess(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetGpaCallback.onFailure(error);
            }
        });
    }

    @Override
    public void getGpaWithToken(String authorization, String token, String captcha, final OnGetGpaCallback onGetGpaCallback) {
        ApiClient.getGpaWithToken(authorization, token, captcha, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                onGetGpaCallback.onSuccess(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetGpaCallback.onFailure(error);
            }
        });
    }

    @Override
    public void refreshToken(String authorization, final OnRefreshTokenCallback onRefreshTokenCallback) {
        ApiClient.refreshToken(authorization, new Callback<RefreshedToken>() {
            @Override
            public void success(RefreshedToken refreshedToken, Response response) {
                onRefreshTokenCallback.onSuccess(refreshedToken);
            }

            @Override
            public void failure(RetrofitError error) {
                onRefreshTokenCallback.onFailure();
            }
        });
    }
}
