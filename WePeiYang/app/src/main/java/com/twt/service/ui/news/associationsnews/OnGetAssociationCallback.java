package com.twt.service.ui.news.associationsnews;

import com.twt.service.bean.NewsList;

/**
 * Created by sunjuntao on 15/11/19.
 */
public interface OnGetAssociationCallback {
    void onSuccess(NewsList newsList);

    void onFailure(String errorMsg);
}
