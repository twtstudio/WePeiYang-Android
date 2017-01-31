package com.twt.service.ui.lostfound.lost;

import com.twt.service.bean.Lost;
import com.twt.service.bean.LostItem;

import java.util.List;

/**
 * Created by Rex on 2015/8/2.
 */
public interface LostView {

    void hideRefreshing();

    void toastMessage(String msg);


    void refreshItems(List<LostItem> items);

    void loadMoreItems(List<LostItem> items);
}
