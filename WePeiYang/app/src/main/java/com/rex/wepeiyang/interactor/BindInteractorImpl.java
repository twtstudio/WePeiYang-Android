package com.rex.wepeiyang.interactor;

import com.google.gson.JsonElement;
import com.rex.wepeiyang.api.ApiClient;
import com.rex.wepeiyang.ui.bind.OnBindCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 16/1/1.
 */
public class BindInteractorImpl implements BindInteractor {
    @Override
    public void bind(String authorization,String tjuname, String tjupwd, final OnBindCallback onBindCallback) {
        ApiClient.bind(authorization, tjuname, tjupwd, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                onBindCallback.onFailure(error);
            }
        });
    }
}
