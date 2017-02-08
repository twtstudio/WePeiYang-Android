package com.twtstudio.retrox.schedule.view;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.schedule.ResourceHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;

/**
 * Created by retrox on 2017/2/8.
 */

public class CourseEmptyViewModel implements ViewModel {
    public final ObservableField<String> courseName = new ObservableField<>();

    public final ObservableInt cardColor = new ObservableInt();

    public final ObservableInt textColor = new ObservableInt();

    public CourseEmptyViewModel(ClassTable.Data.Course course) {
        courseName.set(course.coursename);
        this.cardColor.set(ResourceHelper.getColor(course.coursecolor));
        if (course.coursecolor == com.twtstudio.retrox.schedule.R.color.myWindowBackgroundGray){
            textColor.set(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.schedule_gray));
        }else {
            textColor.set(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.white_color));
        }

    }
}
