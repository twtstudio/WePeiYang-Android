package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.LostDetails;
import com.twt.service.ui.lostfound.lost.details.FailureEvent;
import com.twt.service.ui.lostfound.lost.details.SuccessEvent;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rex on 2015/8/2.
 */
public class LostInteractorImpl implements LostInteractor {

    @Override
    public void getLostList(int page) {

    }

    @Override
    public void getLostDetails(int id) {
        ApiClient.getLostDetails(id, new Callback<LostDetails>() {
            @Override
            public void success(LostDetails lostDetails, Response response) {
                EventBus.getDefault().post(new SuccessEvent(lostDetails));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FailureEvent(error));
            }
        });
    }
}
