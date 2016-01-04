package com.rex.wepeiyang.ui.news.importantnews;

import com.rex.wepeiyang.bean.NewsItem;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/14.
 */
public interface ImportantNewsView {
    void showToast(String message);
    void loadMoreItems(List<NewsItem> items);
    void refreshItems(List<NewsItem> items);
}
