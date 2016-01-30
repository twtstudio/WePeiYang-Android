package com.twt.service.interactor;
import com.google.gson.JsonElement;
import com.twt.service.api.ApiClient;
import com.twt.service.ui.main.FailureEvent;
import com.twt.service.ui.main.OnGetMainCallback;
import com.twt.service.ui.main.SuccessEvent;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/12/3.
 */
public class MainInteractorImpl implements MainInteractor {
    @Override
    public void getMain(final OnGetMainCallback onGetMainCallback) {
        ApiClient.getMain(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement mainJson, Response response) {
                EventBus.getDefault().post(new SuccessEvent(mainJson.toString()));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FailureEvent(error));
            }
        });
    }
}
