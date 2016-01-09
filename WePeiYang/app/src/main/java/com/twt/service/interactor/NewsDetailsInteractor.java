package com.twt.service.interactor;

import com.twt.service.ui.news.details.OnGetNewsDetailsCallback;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface NewsDetailsInteractor {
    void getNewsDetails(int index, OnGetNewsDetailsCallback onGetNewsDetailsCallback);
}
