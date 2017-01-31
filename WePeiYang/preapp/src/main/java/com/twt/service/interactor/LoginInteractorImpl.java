package com.twt.service.interactor;

import android.util.Log;

import com.google.gson.JsonElement;
import com.twt.service.api.ApiClient;
import com.twt.service.ui.login.FailureEvent;
import com.twt.service.ui.login.OnLoginCallback;
import com.twt.service.ui.login.SuccessEvent;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 16/1/1.
 */
public class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void login(String twtuname, String twtpasswd, final OnLoginCallback onLoginCallback) {
        ApiClient.login(twtuname, twtpasswd, new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {
                EventBus.getDefault().post(new SuccessEvent(jsonElement.toString()));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FailureEvent(error));
            }
        });
    }
}
