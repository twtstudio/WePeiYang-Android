package com.twtstudio.retrox.schedule.view;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.kelin.mvvmlight.base.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.twtstudio.retrox.schedule.TimeHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rx.Observable;

/**
 * Created by zhangyulong on 7/3/17.
 */

public class SelectedDateInfoViewModel implements ViewModel {
    public final ObservableField<String> today = new ObservableField<>();

    public final ObservableField<String> date = new ObservableField<>();


    public SelectedDateInfoViewModel(CalendarDay calendarDay) {
        initData(calendarDay);
    }

    private void initData(CalendarDay calendarDay){
        Log.d("today",CalendarDay.today().toString());
        if(calendarDay.equals(CalendarDay.today()))
        today.set("今天");
        else
            today.set("所选日期");
        date.set(calendarDay.getYear()+"年"+(calendarDay.getMonth()+1)+"月"+calendarDay.getDay()+"日");


    }
}
