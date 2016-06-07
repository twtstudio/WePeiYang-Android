package com.twt.service.ui.news.importantnews;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/14.
 */
public interface ImportantNewsView {
    void showToast(String message);
    void loadMoreItems(List<NewsItem> items);
    void refreshItems(List<NewsItem> items);
    void setRefreshEnable(boolean refreshEnable);
}
