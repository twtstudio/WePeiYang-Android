package com.twt.service.ui.schedule;

import com.twt.service.bean.ClassTable;

/**
 * Created by sunjuntao on 15/12/5.
 */
public interface ScheduleView {
    void toastMessage(String msg);
    void showProgress();
    void hideProgress();
    void bindData(ClassTable classTable);
    void startLoginActivity();
    void startBindActivity();
}
