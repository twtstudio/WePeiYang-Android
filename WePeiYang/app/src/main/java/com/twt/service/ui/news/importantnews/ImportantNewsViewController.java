package com.twt.service.ui.news.importantnews;

import com.twt.service.common.IViewController;
import com.twt.service.model.NewsItem;

import java.util.List;

/**
 * Created by sunjuntao on 16/6/6.
 */
public interface ImportantNewsViewController extends IViewController {
    void loadMoreItems(List<NewsItem> items);

    void refreshItems(List<NewsItem> items);
}
