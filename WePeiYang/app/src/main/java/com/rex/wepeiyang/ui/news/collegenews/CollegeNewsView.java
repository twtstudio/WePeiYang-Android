package com.rex.wepeiyang.ui.news.collegenews;

import com.rex.wepeiyang.bean.NewsItem;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/19.
 */
public interface CollegeNewsView {
    void toastMessage(String message);
    void hideFooter();
    void showRefreshing();
    void hideRefreshing();
    void useFooter();
    void loadMoreItems(List<NewsItem> items);
    void refreshItems(List<NewsItem> items);
}
