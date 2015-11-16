package com.rex.wepeiyang.ui.news.details;

import com.rex.wepeiyang.bean.News;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface OnGetNewsDetailsCallback {
    void onSuccess(News news);
    void onFailure(String errorMsg);
}
