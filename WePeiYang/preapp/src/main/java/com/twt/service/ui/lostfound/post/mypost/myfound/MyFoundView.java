package com.twt.service.ui.lostfound.post.mypost.myfound;

import com.twt.service.bean.FoundItem;

import java.util.List;

/**
 * Created by sunjuntao on 16/4/5.
 */
public interface MyFoundView {
    void hideRefreshing();

    void toastMessage(String msg);

    void refreshItems(List<FoundItem> items);

    void loadMoreItems(List<FoundItem> items);

    void startLoginActivity();
}
