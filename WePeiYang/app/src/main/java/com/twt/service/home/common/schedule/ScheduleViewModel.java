package com.twt.service.home.common.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.preference.PreferenceManager;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.ScheduleActivity;
import com.twtstudio.retrox.schedule.TimeHelper;
import com.twtstudio.retrox.schedule.view.ScheduleTodayAct;
import com.twt.service.BR;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.model.CourseHelper;
import com.twt.service.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observable;

/**
 * Created by retrox on 2017/2/7.
 */

public class ScheduleViewModel implements ViewModel {

    private RxAppCompatActivity rxAppCompatActivity;

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_common_course);

    public final ReplyCommand replyCommand = new ReplyCommand(this::jumpTodayDetail);

    public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);


    public ScheduleViewModel(RxAppCompatActivity rxActivity) {
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
        Logger.d(items);
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
        title.set(stringBuilder.toString());
    }

    private void jumpTodayDetail() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rxAppCompatActivity);
        boolean isShowTodayCoursePage = sharedPreferences.getBoolean(rxAppCompatActivity.getString(R.string.pref_is_show_today_course), false);

        if (isShowTodayCoursePage) {
            Intent intent = new Intent(rxAppCompatActivity, ScheduleTodayAct.class);
            rxAppCompatActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(rxAppCompatActivity, ScheduleActivity.class);
            rxAppCompatActivity.startActivity(intent);
        }

    }
}
