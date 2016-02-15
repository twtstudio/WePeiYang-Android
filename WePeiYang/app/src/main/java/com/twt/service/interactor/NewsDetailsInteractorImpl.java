package com.twt.service.interactor;

import android.net.wifi.WifiManager;

import com.google.gson.JsonElement;
import com.twt.service.api.ApiClient;
import com.twt.service.bean.CommentCallback;
import com.twt.service.bean.News;
import com.twt.service.ui.news.comments.FailureEvent;
import com.twt.service.ui.news.comments.SuccessEvent;
import com.twt.service.ui.news.details.OnGetNewsDetailsCallback;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/11/16.
 */
public class NewsDetailsInteractorImpl implements NewsDetailsInteractor {
    @Override
    public void getNewsDetails(int index, final OnGetNewsDetailsCallback onGetNewsDetailsCallback) {
        ApiClient.getNewsDetails(index, new Callback<News>() {
            @Override
            public void success(News news, Response response) {
                onGetNewsDetailsCallback.onSuccess(news);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetNewsDetailsCallback.onFailure(error);
            }
        });
    }

    @Override
    public void postComment(String authorization, int id, String content, String ip) {
        ApiClient.postNewsComment(authorization, id, content, ip, new Callback<CommentCallback>() {
            @Override
            public void success(CommentCallback callback, Response response) {
                EventBus.getDefault().post(new SuccessEvent(callback));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new FailureEvent(error));
            }
        });
    }
}
