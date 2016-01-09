package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.Login;
import com.twt.service.ui.login.OnLoginCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 16/1/1.
 */
public class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void login(String twtuname, String twtpasswd, final OnLoginCallback onLoginCallback) {
        ApiClient.login(twtuname, twtpasswd, new Callback<Login>() {

            @Override
            public void success(Login login, Response response) {
                onLoginCallback.onSuccess(login);
            }

            @Override
            public void failure(RetrofitError error) {
                onLoginCallback.onFailure(error);
            }
        });
    }
}
