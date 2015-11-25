package com.rex.wepeiyang.ui.news.collegenews;

import com.rex.wepeiyang.bean.NewsList;

/**
 * Created by sunjuntao on 15/11/19.
 */
public interface OnGetCollegeNewsCallback {
    void onSuccess(NewsList newsList);
    void onFailure(String errorMsg);
}
