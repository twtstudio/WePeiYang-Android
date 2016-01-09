package com.twt.service.ui.lostfound.found;

/**
 * Created by RexSun on 15/8/16.
 */
public interface FoundView {
    void showRefreshing();
    void hideRefreshing();
    void toastMessage(String msg);
}
