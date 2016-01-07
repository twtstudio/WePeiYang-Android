package com.rex.wepeiyang.interactor;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rex.wepeiyang.api.ApiClient;
import com.rex.wepeiyang.bean.Error;
import com.rex.wepeiyang.ui.gpa.OnGetGpaCallback;
import com.rex.wepeiyang.ui.gpa.OnRefreshTokenCallback;

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
                com.rex.wepeiyang.bean.Error jsonError = new Gson().fromJson(error.getBody().toString(), Error.class);
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
    public void refreshToken(String authorization, OnRefreshTokenCallback onRefreshTokenCallback) {
        ApiClient.refreshToken(authorization, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
