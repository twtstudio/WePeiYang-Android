package com.twt.service.interactor;

/**
 * Created by sunjuntao on 15/11/14.
 */
public interface ImportantNewsInteractor {
    void getImportantNews(int page, OnGetImportantNewsCallback onGetImportantNewsCallback);
}
