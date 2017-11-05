package com.twtstudio.retrox.schedule.view;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.kelin.mvvmlight.base.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * Created by zhangyulong on 7/3/17.
 */

public class SelectedDateInfoViewModel implements ViewModel {
    public final MutableLiveData<String> today = new MutableLiveData<>();

    public final MutableLiveData<String> date = new MutableLiveData<>();


    public SelectedDateInfoViewModel(CalendarDay calendarDay) {
        initData(calendarDay);
    }

    private void initData(CalendarDay calendarDay){
        Log.d("today",CalendarDay.today().toString());
        if(calendarDay.equals(CalendarDay.today()))
        today.setValue("今天");
        else
            today.setValue("所选日期");
        date.setValue(calendarDay.getYear()+"年"+(calendarDay.getMonth()+1)+"月"+calendarDay.getDay()+"日");


    }
}
