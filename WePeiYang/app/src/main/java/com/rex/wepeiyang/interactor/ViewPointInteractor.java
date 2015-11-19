package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.news.viewpoint.OnGetViewPointCallback;

/**
 * Created by sunjuntao on 15/11/18.
 */
public interface ViewPointInteractor {
    void getViewPoints(int page, OnGetViewPointCallback onGetViewPointCallback);
}
