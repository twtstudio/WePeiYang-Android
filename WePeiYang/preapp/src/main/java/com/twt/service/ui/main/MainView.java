package com.twt.service.ui.main;

import com.twt.service.bean.Main;

/**
 * Created by sunjuntao on 15/12/3.
 */
public interface MainView {
    void toastMessage(String msg);
    void bindData(Main main);
    void hideRefreshing();
}
