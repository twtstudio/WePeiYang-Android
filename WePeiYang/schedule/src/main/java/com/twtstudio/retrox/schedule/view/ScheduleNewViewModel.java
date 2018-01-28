package com.twtstudio.retrox.schedule.view;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.ScheduleNewActivity;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.model.CourseHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangyulong on 7/3/17.
 */

public class ScheduleNewViewModel extends AndroidViewModel implements ViewModel{
    private RxAppCompatActivity rxActivity;
    public final ArrayList<ViewModel> items = new ArrayList<>();
//    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
//            .put(SelectedCoursesInfoViewModel.class, BR.viewModel, R.layout.item_selected_courses)
//            .put(SelectedDateInfoViewModel.class,BR.viewModel,R.layout.item_selected_date)
//            .put(CourseIsEmptyViewModel.class,BR.viewModel,R.layout.schedule_item_course_is_empty)
//            .build();
    public final MutableLiveData<String> today = new MutableLiveData<>();

    public final MutableLiveData<String> date = new MutableLiveData<>();
    public final CourseHelper courseHelper = new CourseHelper();

    public ScheduleNewViewModel(RxAppCompatActivity rxActivity, CalendarDay calendarDay) {
        super(rxActivity.getApplication());
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
//        setDate(calendarDay);
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
        if(rxActivity instanceof ScheduleNewActivity)
            ((ScheduleNewActivity) rxActivity).updateAdapter(items);

    }
//    private void setDate(CalendarDay calendarDay){
//        if(calendarDay.equals(CalendarDay.today()))
//            today.setValue("今天");
//        else
//            today.setValue("所选日期");
//        date.setValue(calendarDay.getYear()+"年"+(calendarDay.getMonth()+1)+"月"+calendarDay.getDay()+"日");
//    }



}
