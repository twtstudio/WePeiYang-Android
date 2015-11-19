package com.rex.wepeiyang.ui.news.associations;

import com.rex.wepeiyang.bean.NewsList;

/**
 * Created by sunjuntao on 15/11/19.
 */
public interface OnGetAssociationCallback {
    void onSuccess(NewsList newsList);

    void onFailure(String errorMsg);
}
