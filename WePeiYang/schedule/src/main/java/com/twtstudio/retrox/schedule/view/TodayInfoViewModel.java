package com.twtstudio.retrox.schedule.view;

import android.arch.lifecycle.MutableLiveData;

import com.twtstudio.retrox.schedule.TimeHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rx.Observable;

/**
 * Created by retrox on 2017/2/8.
 */

public class TodayInfoViewModel implements ViewModel {

    public final MutableLiveData<String> date = new MutableLiveData<>();

    public final MutableLiveData<String> weekNumber = new MutableLiveData<>();

    public final MutableLiveData<String> todayNumber = new MutableLiveData<>();

    public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);

    public TodayInfoViewModel(ClassTable classTable) {
        initData(classTable);
    }

    private void initData(ClassTable classTable){
        Observable.just(Calendar.getInstance())
                .map(Calendar::getTime)
                .map(dateFormate::format)
                .subscribe(date::setValue);
        String s = "星期"+ TimeHelper.getChineseCharacter(CourseHelper.getTodayNumber());
        todayNumber.setValue(s);
        weekNumber.setValue("第"+TimeHelper.getWeekString(TimeHelper.getWeekInt(Long.parseLong(classTable.data.term_start),Calendar.getInstance()))+"周");
    }
}
