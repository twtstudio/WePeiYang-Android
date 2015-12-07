package com.rex.wepeiyang.ui.news.viewpoint;

import com.rex.wepeiyang.bean.NewsItem;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/18.
 */
public interface ViewPointView {
    void showProgress();

    void hideProgress();

    void toastMessage(String msg);

    void refreshItems(List<NewsItem> items);

    void loadMoreItems(List<NewsItem> items);
}
