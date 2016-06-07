package com.twt.service.ui.notice;

import java.util.List;

/**
 * Created by sunjuntao on 15/11/16.
 */
public interface NoticeView {
    void showProgress();
    void hideProgress();
    void useFooter();
    void hideFooter();
    void toastMessage(String msg);
    void refreshItems(List<NewsItem> items);
    void loadMoreItems(List<NewsItem> items);
    void setRefreshEnable(boolean refreshEnable);
}
