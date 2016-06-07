package com.twt.service.ui.news.associationsnews;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/19.
 */
public interface AssociationView {
    void showToast(String message);

    void loadMoreItems(List<NewsItem> items);

    void refreshItems(List<NewsItem> items);

    void setRefreshEnable(boolean refreshEnable);
}
