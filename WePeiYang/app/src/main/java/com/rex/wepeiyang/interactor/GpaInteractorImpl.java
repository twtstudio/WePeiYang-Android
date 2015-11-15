package com.rex.wepeiyang.interactor;

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
    public void getGpa(String tjuuname, String tjupasswd, final OnGetGpaCallback onGetGpaCallback) {
        ApiClient.getGpa(tjuuname, tjupasswd, new Callback<Gpa>() {
            @Override
            public void success(Gpa gpa, Response response) {
                onGetGpaCallback.onSuccess(gpa);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetGpaCallback.onFailure(error.toString());
            }
        });
    }
}
