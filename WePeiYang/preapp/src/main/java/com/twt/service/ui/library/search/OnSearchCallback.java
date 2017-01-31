package com.twt.service.ui.library.search;

import com.twt.service.bean.LibSearch;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/15.
 */
public interface OnSearchCallback {
    void onSuccess(LibSearch libSearch);
    void onFailure(RetrofitError error);
}
