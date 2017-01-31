package com.twt.service.interactor;

import com.twt.service.ui.news.importantnews.OnGetImportantNewsCallback;

/**
 * Created by sunjuntao on 15/11/14.
 */
public interface ImportantNewsInteractor {
    void getImportantNews(int page, OnGetImportantNewsCallback onGetImportantNewsCallback);
}
