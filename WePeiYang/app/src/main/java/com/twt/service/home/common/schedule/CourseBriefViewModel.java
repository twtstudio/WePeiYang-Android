package com.twt.service.home.common.schedule;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.schedule.ResourceHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;

/**
 * Created by retrox on 2017/2/7.
 */

public class CourseBriefViewModel implements ViewModel {

    public final ObservableField<String> courseName = new ObservableField<>();

    public final ObservableInt cardColor = new ObservableInt();

    public final ObservableInt textColor = new ObservableInt();

    public CourseBriefViewModel(ClassTable.Data.Course course) {
        String name = course.coursename;
        int color = course.coursecolor;
        if (!name.equals("无")) {
            this.courseName.set(name + "@" + CourseHelper.getTodayLocation(course.arrange));
        } else {
            this.courseName.set(name);
        }
        this.cardColor.set(ResourceHelper.getColor(color));
        if (color == com.twtstudio.retrox.schedule.R.color.myWindowBackgroundGray) {
            textColor.set(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.schedule_gray));
        } else {
            textColor.set(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.white_color));
        }
    }

    public CourseBriefViewModel() {
    }

    public static CourseBriefViewModel getInstance(ClassTable.Data.Course course) {
        return new CourseBriefViewModel(course);
    }
}
