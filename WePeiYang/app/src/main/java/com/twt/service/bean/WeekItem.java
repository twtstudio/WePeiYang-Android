package com.twt.service.bean;

import com.twt.service.ui.schedule.TimeHelper;

/**
 * Created by cmj on 2016/3/29.
 */
public class WeekItem {
    private String week;
    private int week_num;
    private boolean active;

    public WeekItem(int week, boolean active) {
        week_num = week;
        this.week = "第" + TimeHelper.getWeekString(week) + "周";
        this.active = active;
    }

    public int getWeek_num() {
        return week_num;
    }

    public String getTitle() {
        return week;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
