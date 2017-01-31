package com.twt.service.interactor;

import com.google.gson.JsonElement;
import com.twt.service.api.ApiClient;
import com.twt.service.ui.account.AccountPresenter;
import com.twt.service.ui.account.FailureEvent;
import com.twt.service.ui.account.SuccessEvent;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 16/1/28.
 */
public class AccountInteractorImpl implements AccountInteractor {

    @Override
    public void unbind(String authorization, String twtuanme) {
        ApiClient.unbind(authorization, twtuanme, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                EventBus.getDefault().post(new SuccessEvent());
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FailureEvent(error));
            }
        });
    }
}
