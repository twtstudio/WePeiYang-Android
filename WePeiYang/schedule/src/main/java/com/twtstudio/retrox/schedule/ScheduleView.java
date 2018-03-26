package com.twtstudio.retrox.schedule;

import com.twtstudio.retrox.schedule.model.ClassTable;

/**
 * Created by sunjuntao on 15/12/5.
 */
public interface ScheduleView {
    void toastMessage(String msg);
    void showProgress();
    void hideProgress();
    void bindData(ClassTable classTable);
    //试试能不能改

}
