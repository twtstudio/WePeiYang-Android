package com.rex.wepeiyang.ui.news.associationsnews;

import com.rex.wepeiyang.bean.NewsItem;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/19.
 */
public interface AssociationView {
    void showToast(String message);

    void hideFooter();

    void showRefreshing();

    void hideRefreshing();

    void userFooter();

    void loadMoreItems(List<NewsItem> items);

    void refreshItems(List<NewsItem> items);
}
