package com.twt.service.home.common.schedule;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.TimeHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.model.CourseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rx.Observable;

/**
 * Created by retrox on 2017/2/7.
 */

public class ScheduleViewModel extends AndroidViewModel implements ViewModel {

    public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
    public final MutableLiveData<String> title = new MutableLiveData<>();

    public final List<ViewModel> items = new ArrayList<>();

    public final MutableLiveData<List<ViewModel>> liveItems = new MutableLiveData<>();
    private RxAppCompatActivity rxAppCompatActivity;


    public ScheduleViewModel(RxAppCompatActivity rxActivity) {
        super(rxActivity.getApplication());
        this.rxAppCompatActivity = rxActivity;
        getTodayString();
        getData();
    }

    public void getData() {
        ClassTableProvider.init(rxAppCompatActivity)
                .registerAction(this::handleClassTable)
                .getData();
    }

    private void handleClassTable(ClassTable classTable) {
        CourseHelper.setCalendar(CalendarDay.today());
        List<ClassTable.Data.Course> courseList = new CourseHelper().getTodayCourses(classTable, true);
        for (ClassTable.Data.Course course : courseList) {
            items.add(new CourseBriefViewModel(course));
        }
        liveItems.setValue(items);
//        Hawk.put("itemstest",items);
//        Logger.d(items);
    }

    private void getTodayString() {
        StringBuilder stringBuilder = new StringBuilder();
        Observable.just(Calendar.getInstance())
                .map(Calendar::getTime)
                .map(dateFormate::format)
                .subscribe(stringBuilder::append);
        stringBuilder.append("  ");
        String s = "星期" + TimeHelper.getChineseCharacter(new CourseHelper().getTodayNumber());
        stringBuilder.append(s);
        title.setValue(stringBuilder.toString());
    }

}
