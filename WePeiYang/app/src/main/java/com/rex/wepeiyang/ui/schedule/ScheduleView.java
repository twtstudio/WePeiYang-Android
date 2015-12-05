package com.rex.wepeiyang.ui.schedule;

import com.rex.wepeiyang.bean.ClassTable;

/**
 * Created by sunjuntao on 15/12/5.
 */
public interface ScheduleView {
    void toastMessage(String msg);
    void showProgress();
    void hideProgress();
    void bindData(ClassTable classTable);
}
