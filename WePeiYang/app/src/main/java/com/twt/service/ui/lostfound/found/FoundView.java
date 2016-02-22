package com.twt.service.ui.lostfound.found;

import com.twt.service.bean.FoundItem;

import java.util.List;

/**
 * Created by RexSun on 15/8/16.
 */
public interface FoundView {
    void hideRefreshing();

    void toastMessage(String msg);

    void refreshItems(List<FoundItem> items);

    void loadMoreItems(List<FoundItem> items);
}
