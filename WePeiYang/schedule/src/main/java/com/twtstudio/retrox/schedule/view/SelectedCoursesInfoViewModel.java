package com.twtstudio.retrox.schedule.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.schedule.R;
import com.twtstudio.retrox.schedule.ScheduleDetailsActivity;
import com.twtstudio.retrox.schedule.ScheduleNewActivity;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;

/**
 * Created by zhangyulong on 7/3/17.
 */

public class SelectedCoursesInfoViewModel implements ViewModel {
    public final ObservableField<String> timePeriod = new ObservableField<>();
    public final ObservableField<String> mTime = new ObservableField<>();
    public final ObservableField<String> courseName = new ObservableField<>();
    public final ObservableField<String> location = new ObservableField<>();
    private Context mContext;
    private ClassTable.Data.Course course;
    private String timeStart, timeEnd;
    private final CourseHelper courseHelper = new CourseHelper();
    public  void onClick(View view){
        Intent intent=new Intent(mContext,ScheduleDetailsActivity.class);
        intent.putExtra("color", R.color.schedule_primary_color);
        intent.putExtra("course",course);
        mContext.startActivity(intent);
    }

    SelectedCoursesInfoViewModel(ClassTable.Data.Course course,Context mContext) {
        this.mContext=mContext;
        initData(course);
    }

    private void initData(ClassTable.Data.Course course) {
        String name = course.coursename;
        this.course=course;
        if (!name.equals("无")) {
            this.courseName.set(name);
            this.location.set(CourseHelper.getTodayLocation(course.arrange));
        } else {
            this.courseName.set(name);
        }

        timePeriod.set("第" + courseHelper.getTodayStart(course.arrange) + "-" + courseHelper.getTodayEnd(course.arrange) + "节");
        switch (courseHelper.getTodayStart(course.arrange)) {
            case 1:
                timeStart = "8:30";
                break;
            case 3:
                timeStart = "10:30";
                break;
            case 5:
                timeStart = "13:30";
                break;
            case 7:
                timeStart = "15:25";
                break;
            case 9:
                timeStart = "18:30";
                break;
            case 11:
                timeStart = "20:20";
                break;
        }
        switch (courseHelper.getTodayEnd(course.arrange)) {
            case 2:
                timeEnd = "10:00";
                break;
            case 4:
                timeEnd = "12:00";
                break;
            case 6:
                timeEnd = "15:05";
                break;
            case 8:
                timeEnd = "17:00";
                break;
            case 10:
                timeEnd = "20:10";
                break;
            case 11:
                timeEnd = "21:05";
                break;
            case 12:
                timeEnd = "22:00";
                break;
        }
        mTime.set(timeStart+"  -  "+timeEnd);
    }


}
