package com.twt.service.ui.news.collegenews;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/19.
 */
public interface CollegeNewsView {
    void toastMessage(String message);
    void loadMoreItems(List<NewsItem> items);
    void refreshItems(List<NewsItem> items);
    void setRefreshEnable(boolean refreshEnable);
}
