package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.LibSearch;
import com.twt.service.ui.library.search.OnSearchCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jcy on 2016/7/18.
 */

public class LibSearchInteractorImpl implements LibSearchInteractor {
    @Override
    public void libSearch(String title, int page, final OnSearchCallback onSearchCallback) {
        ApiClient.libSearch(title, page, new Callback<LibSearch>() {
            @Override
            public void success(LibSearch libSearch, Response response) {
                onSearchCallback.onSuccess(libSearch);
            }

            @Override
            public void failure(RetrofitError error) {
                onSearchCallback.onFailure(error);
            }
        });
    }
}
