package com.twt.service.interactor;


import com.google.gson.JsonElement;
import com.twt.service.api.ApiClient;
import com.twt.service.bean.Lost;
import com.twt.service.bean.LostDetails;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.lostfound.lost.details.FailureEvent;
import com.twt.service.ui.lostfound.lost.details.SuccessEvent;
import com.twt.service.ui.lostfound.post.lost.event.EditLostFailureEvent;
import com.twt.service.ui.lostfound.post.lost.event.EditLostSuccessEvent;
import com.twt.service.ui.lostfound.post.mypost.mylost.event.GetMyLostFailureEvent;
import com.twt.service.ui.lostfound.post.mypost.mylost.event.GetMyLostSuccessEvent;

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
        ApiClient.getLostList(page, new Callback<Lost>() {
            @Override
            public void success(Lost lost, Response response) {
                EventBus.getDefault().post(new com.twt.service.ui.lostfound.lost.SuccessEvent(lost));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new com.twt.service.ui.lostfound.lost.FailureEvent(error));
            }
        });
    }

    @Override
    public void getLostDetails(int id) {
        ApiClient.getLostDetails(id, new Callback<LostDetails>() {
            @Override
            public void success(LostDetails lostDetails, Response response) {
                EventBus.getDefault().post(lostDetails);
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FailureEvent(error));
            }
        });
    }

    @Override
    public void postLost(String authorization, String title, String name, String time, String place, String phone, String content, String lost_type, String otherTag) {
        ApiClient.postLost(authorization, title, name, time, place, phone, content, lost_type, otherTag, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                EventBus.getDefault().post(new com.twt.service.ui.lostfound.post.lost.event.SuccessEvent());
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new com.twt.service.ui.lostfound.post.lost.event.FailureEvent(error));
            }
        });
    }

    @Override
    public void getMyLostList(String authorization, int page) {
        ApiClient.getMyLostList(authorization, page, new Callback<Lost>() {
            @Override
            public void success(Lost lost, Response response) {
                EventBus.getDefault().post(new GetMyLostSuccessEvent(lost));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new GetMyLostFailureEvent(error));
            }
        });
    }

    @Override
    public void editLost(String authorization, int id, String title, String name, String time, String place, String phone, String content, int lost_type, String other_tag) {
        ApiClient.editLost(authorization, id, title, name, time, place, phone, content, lost_type, other_tag, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                EventBus.getDefault().post(new EditLostSuccessEvent());
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new EditLostFailureEvent(error));
            }
        });
    }
}
