package com.twt.service.ui.lostfound.post.mypost.mylost;

import com.twt.service.bean.LostItem;

import java.util.List;

/**
 * Created by sunjuntao on 16/4/5.
 */
public interface MyLostView {
    void hideRefreshing();

    void toastMessage(String msg);


    void refreshItems(List<LostItem> items);

    void loadMoreItems(List<LostItem> items);

    void startLoginActivity();
}
