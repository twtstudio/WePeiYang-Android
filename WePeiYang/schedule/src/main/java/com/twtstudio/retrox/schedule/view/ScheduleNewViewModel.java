package com.twtstudio.retrox.schedule.view;

import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;

import com.kelin.mvvmlight.base.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.BR;
import com.twtstudio.retrox.schedule.R;
import com.twtstudio.retrox.schedule.databinding.ActivityScheduleNewBinding;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.model.CourseHelper;

import java.util.List;
import java.util.Objects;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by zhangyulong on 7/3/17.
 */

public class ScheduleNewViewModel {
    private RxAppCompatActivity rxActivity;
    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(SelectedCoursesInfoViewModel.class, BR.viewModel, R.layout.item_selected_courses)
            .put(SelectedDateInfoViewModel.class,BR.viewModel,R.layout.item_selected_date)
            .put(CourseIsEmptyViewModel.class,BR.viewModel,R.layout.schedule_item_course_is_empty)
            .build();
    public final ObservableField<String> today = new ObservableField<>();

    public final ObservableField<String> date = new ObservableField<>();

    public final CourseHelper courseHelper = new CourseHelper();

    public ScheduleNewViewModel(RxAppCompatActivity rxActivity, CalendarDay calendarDay) {

        this.rxActivity = rxActivity;
        initData(calendarDay);
    }

    public void initData(CalendarDay calendarDay) {
        ClassTableProvider.init(rxActivity)
                .registerAction2(this::processData)
                .getData(calendarDay);
    }


    private void processData(ClassTable classTable,CalendarDay calendarDay) {
        items.clear();
        courseHelper.setCalendar(calendarDay);
        setDate(calendarDay);
        items.add(new SelectedDateInfoViewModel(calendarDay));
        List<ClassTable.Data.Course> courseList = courseHelper.getTodayCourses(classTable, false);
        courseHelper.setCalendar(calendarDay);
        for (int i = courseList.size() - 1; i >= 0; i--) {
                //去除后面结尾的 "无" 空课程
                if (courseList.get(i).coursename.equals("无")) {
                    courseList.remove(i);
                } else {
                    break;
                }
            }
        for (ClassTable.Data.Course course : courseList) {
            if (!course.coursename.equals("无")){
                items.add(new SelectedCoursesInfoViewModel(course, rxActivity));

            }

        }
        if(items.size()==1){
            items.add(new CourseIsEmptyViewModel(calendarDay,classTable));
        }

    }
    private void setDate(CalendarDay calendarDay){
        if(calendarDay.equals(CalendarDay.today()))
            today.set("今天");
        else
            today.set("所选日期");
        date.set(calendarDay.getYear()+"年"+(calendarDay.getMonth()+1)+"月"+calendarDay.getDay()+"日");
    }



}
