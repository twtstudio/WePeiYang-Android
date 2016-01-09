package com.twt.service.ui.news.viewpoint;

import com.twt.service.bean.NewsItem;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/18.
 */
public interface ViewPointView {

    void toastMessage(String msg);

    void refreshItems(List<NewsItem> items);

    void loadMoreItems(List<NewsItem> items);

    void setRefreshEnable(boolean refreshEnable);
}
