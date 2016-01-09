package com.twt.service.interactor;

import com.twt.service.ui.news.viewpoint.OnGetViewPointCallback;

/**
 * Created by sunjuntao on 15/11/18.
 */
public interface ViewPointInteractor {
    void getViewPoints(int page, OnGetViewPointCallback onGetViewPointCallback);
}
