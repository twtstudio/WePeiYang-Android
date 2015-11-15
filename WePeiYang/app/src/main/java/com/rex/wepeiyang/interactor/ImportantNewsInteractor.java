package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.news.importantnews.OnGetImportantNewsCallback;

/**
 * Created by sunjuntao on 15/11/14.
 */
public interface ImportantNewsInteractor {
    void getImportantNews(int page, OnGetImportantNewsCallback onGetImportantNewsCallback);
}
