package com.twt.service.home.common.schedule;

import android.arch.lifecycle.MutableLiveData;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.schedule.ResourceHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;

/**
 * Created by retrox on 2017/2/7.
 */

public class CourseBriefViewModel implements ViewModel {

    public final MutableLiveData<String> courseName = new MutableLiveData<>();

    public final MutableLiveData<Integer> cardColor = new MutableLiveData<>();

    public final MutableLiveData<Integer> textColor = new MutableLiveData<>();

    public CourseBriefViewModel(ClassTable.Data.Course course) {
        String name = course.coursename;
        int color = course.coursecolor;
        if (!name.equals("无")) {
            this.courseName.setValue(name + "@" + CourseHelper.getTodayLocation(course.arrange));
        } else {
            this.courseName.setValue(name);
        }
        this.cardColor.setValue(ResourceHelper.getColor(color));
        if (color == com.twtstudio.retrox.schedule.R.color.myWindowBackgroundGray) {
            textColor.setValue(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.schedule_gray));
        } else {
            textColor.setValue(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.white_color));
        }
    }

    public CourseBriefViewModel() {
    }

    public static CourseBriefViewModel getInstance(ClassTable.Data.Course course) {
        return new CourseBriefViewModel(course);
    }
}
