package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.Found;
import com.twt.service.bean.FoundDetails;
import com.twt.service.bean.Upload;
import com.twt.service.ui.lostfound.found.details.FailureEvent;
import com.twt.service.ui.lostfound.found.details.SuccessEvent;

import java.io.File;

import de.greenrobot.event.EventBus;
import im.fir.sdk.FIR;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 16/2/20.
 */
public class FoundInteractorImpl implements FoundInteractor {
    @Override
    public void getFoundList(int page) {
        ApiClient.getFoundList(page, new Callback<Found>() {
            @Override
            public void success(Found found, Response response) {
                EventBus.getDefault().post(new com.twt.service.ui.lostfound.found.SuccessEvent(found));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new com.twt.service.ui.lostfound.found.FailureEvent(error));
            }
        });
    }

    @Override
    public void getFoundDetails(int id) {
        ApiClient.getFoundDetails(id, new Callback<FoundDetails>() {
            @Override
            public void success(FoundDetails details, Response response) {
                EventBus.getDefault().post(new SuccessEvent(details));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FailureEvent(error));
            }
        });
    }

    @Override
    public void uploadImage(File file) {
        ApiClient.uploadImage("lostfound", file, new Callback<Upload>() {
            @Override
            public void success(Upload upload, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
