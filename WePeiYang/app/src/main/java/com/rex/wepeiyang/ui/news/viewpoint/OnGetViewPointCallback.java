package com.rex.wepeiyang.ui.news.viewpoint;

import com.rex.wepeiyang.bean.NewsList;

/**
 * Created by sunjuntao on 15/11/18.
 */
public interface OnGetViewPointCallback {
    void onSuccess(NewsList newsList);

    void onFailure(String errorMsg);
}
