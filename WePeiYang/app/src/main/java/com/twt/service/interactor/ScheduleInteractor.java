package com.twt.service.interactor;

import com.twt.service.ui.schedule.OnGetScheduleCallback;
import com.twt.service.ui.schedule.OnRefreshTokenCallback;

/**
 * Created by sunjuntao on 15/12/5.
 */
public interface ScheduleInteractor {
    void getSchedule(String authorization, OnGetScheduleCallback onGetScheduleCallback);
    void refreshToken(String authorization, OnRefreshTokenCallback onRefreshTokenCallback);
}
