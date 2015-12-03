package com.rex.wepeiyang.ui.main;

import com.rex.wepeiyang.bean.Main;

/**
 * Created by sunjuntao on 15/12/3.
 */
public interface MainView {
    void toastMessage(String msg);
    void showProgress();
    void hideProgress();
    void bindData(Main main);
}
