package com.rex.wepeiyang.ui.notice;

import com.rex.wepeiyang.bean.NewsItem;
import com.rex.wepeiyang.bean.NewsList;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface OnGetNoticeCallback {
    void onSuccess(NewsList newsList);
    void onFailure(String errorMsg);
}
