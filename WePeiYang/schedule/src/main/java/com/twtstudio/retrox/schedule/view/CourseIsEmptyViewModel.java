package com.twtstudio.retrox.schedule.view;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * Created by zhangyulong on 7/9/17.
 */

public class CourseIsEmptyViewModel implements ViewModel {
    public final ObservableField<String> text = new ObservableField<>();
    private String day;

    CourseIsEmptyViewModel(CalendarDay calendarDay) {
        if ((calendarDay.getMonth() >= 2 && calendarDay.getMonth() <6)
                || (calendarDay.getMonth() >= 8 || calendarDay.getMonth() < 1))
            text.set("暂时无法获取此学期的课表");
        else {
            if (calendarDay.equals(CalendarDay.today()))
                day = "今日";
            else day = "该日";
            text.set(day + "无课，做点有意思的事吧~");
        }
    }
}
