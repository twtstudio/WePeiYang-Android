package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.api.ApiClient;
import com.rex.wepeiyang.bean.Login;
import com.rex.wepeiyang.ui.login.OnLoginCallback;

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
