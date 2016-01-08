package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.schedule.OnGetScheduleCallback;
import com.rex.wepeiyang.ui.schedule.OnRefreshTokenCallback;

/**
 * Created by sunjuntao on 15/12/5.
 */
public interface ScheduleInteractor {
    void getSchedule(String authorization, OnGetScheduleCallback onGetScheduleCallback);
    void refreshToken(String authorization, OnRefreshTokenCallback onRefreshTokenCallback);
}
