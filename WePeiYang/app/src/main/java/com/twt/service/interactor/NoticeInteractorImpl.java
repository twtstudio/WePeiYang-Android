package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.NewsList;
import com.twt.service.ui.notice.OnGetNoticeCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/11/16.
 */
public class NoticeInteractorImpl implements NoticeInteractor {
    @Override
    public void getNotice(int page, final OnGetNoticeCallback onGetNoticeCallback) {
        ApiClient.getNoticeList(page, new Callback<NewsList>() {
            @Override
            public void success(NewsList newsList, Response response) {
                onGetNoticeCallback.onSuccess(newsList);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetNoticeCallback.onFailure("无法连接到网络");
            }
        });
    }
}
