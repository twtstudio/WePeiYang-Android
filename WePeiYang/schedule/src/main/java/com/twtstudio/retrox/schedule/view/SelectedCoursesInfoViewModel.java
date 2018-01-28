package com.twtstudio.retrox.schedule.view;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.R;
import com.twtstudio.retrox.schedule.ResourceHelper;
import com.twtstudio.retrox.schedule.ScheduleDetailsActivity;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;

/**
 * Created by zhangyulong on 7/3/17.
 */

public class SelectedCoursesInfoViewModel extends AndroidViewModel implements ViewModel {
    public final MutableLiveData<String> timePeriod = new MutableLiveData<>();
    public final MutableLiveData<String> mTime = new MutableLiveData<>();
    public final MutableLiveData<String> courseName = new MutableLiveData<>();
    public final MutableLiveData<String> location = new MutableLiveData<>();
    private RxAppCompatActivity mRxActivity;
    private ClassTable.Data.Course course;
    private String timeStart, timeEnd;
    private final CourseHelper courseHelper = new CourseHelper();

    public void onClick(View view) {
        Intent intent = new Intent(mRxActivity, ScheduleDetailsActivity.class);
        intent.putExtra("color", ResourceHelper.getColor(R.color.schedule_primary_color));
        intent.putExtra("course", course);
        intent.putExtra("colorTag", false);
        mRxActivity.startActivity(intent);
    }

    SelectedCoursesInfoViewModel(ClassTable.Data.Course course, RxAppCompatActivity mRxActivity) {
        super(mRxActivity.getApplication());
        this.mRxActivity = mRxActivity;
        initData(course);
    }

    private void initData(ClassTable.Data.Course course) {
        String name = course.coursename;
        this.course = course;
        if (!name.equals("无")) {
            this.courseName.setValue(name);
            this.location.setValue(CourseHelper.getTodayLocation(course.arrange));
        } else {
            this.courseName.setValue(name);
        }

        timePeriod.setValue("第" + courseHelper.getTodayStart(course.arrange) + "-" + courseHelper.getTodayEnd(course.arrange) + "节");
        switch (courseHelper.getTodayStart(course.arrange)) {
            case 1:
                timeStart = "8:30";
                break;
            case 2:
                timeStart = "9:20";
                break;
            case 3:
                timeStart = "10:25";
                break;
            case 4:
                timeStart = "11:15";
                break;
            case 5:
                timeStart = "13:30";
                break;
            case 6:
                timeStart = "14:20";
                break;
            case 7:
                timeStart = "15:25";
                break;
            case 8:
                timeStart = "16:15";
                break;
            case 9:
                timeStart = "18:30";
                break;
            case 10:
                timeStart = "19:20";
                break;
            case 11:
                timeStart = "20:10";
                break;
            case 12:
                timeStart = "21:00";
                break;
        }
        switch (courseHelper.getTodayEnd(course.arrange)) {
            case 1:
                timeEnd = "9:15";
                break;
            case 2:
                timeEnd = "10:05";
                break;
            case 3:
                timeEnd = "11:10";
                break;
            case 4:
                timeEnd = "12:00";
                break;
            case 5:
                timeEnd = "14:15";
                break;
            case 6:
                timeEnd = "15:05";
                break;
            case 7:
                timeEnd = "16:10";
                break;
            case 8:
                timeEnd = "17:00";
                break;
            case 9:
                timeEnd = "19:15";
                break;
            case 10:
                timeEnd = "20:05";
                break;
            case 11:
                timeEnd = "20:55";
                break;
            case 12:
                timeEnd = "21:45";
                break;
        }
        mTime.setValue(timeStart + "  -  " + timeEnd);
    }


}
