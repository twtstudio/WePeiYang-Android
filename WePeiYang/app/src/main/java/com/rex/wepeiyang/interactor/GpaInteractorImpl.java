package com.rex.wepeiyang.interactor;

import android.util.Log;

import com.google.gson.JsonElement;
import com.rex.wepeiyang.api.ApiClient;
import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.ui.gpa.OnGetGpaCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/11/6.
 */
public class GpaInteractorImpl implements GpaInteractor {
    @Override
    public void getGpaWithoutToken(String tjuuname, String tjupasswd, final OnGetGpaCallback onGetGpaCallback) {
        ApiClient.getGpaWithoutToken(tjuuname, tjupasswd, new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {
                onGetGpaCallback.onSuccess(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetGpaCallback.onFailure(error.getMessage());
                Log.e("GPA error", error.toString());
            }
        });
    }

    @Override
    public void getGpaWithToken(String tjuuname, String tjupasswd, String token, String captcha, final OnGetGpaCallback onGetGpaCallback) {
        ApiClient.getGpaWithToken(tjuuname, tjupasswd, token, captcha, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                onGetGpaCallback.onSuccess(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetGpaCallback.onFailure(error.getMessage());
            }
        });
    }
}
