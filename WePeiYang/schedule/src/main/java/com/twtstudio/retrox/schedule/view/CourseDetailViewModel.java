package com.twtstudio.retrox.schedule.view;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.schedule.ResourceHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;

/**
 * Created by retrox on 2017/2/7.
 */

public class CourseDetailViewModel implements ViewModel {

    public final ObservableField<String> courseName = new ObservableField<>();

    public final ObservableInt cardColor = new ObservableInt();

    public final ObservableInt textColor = new ObservableInt();

    public final ObservableField<String> teacherName = new ObservableField<>();

    public final ObservableField<String> credit = new ObservableField<>();

    public final ObservableField<String> location = new ObservableField<>();

    private final CourseHelper courseHelper = new CourseHelper();

    public final ObservableField<String> timePeriod = new ObservableField<>();

    public CourseDetailViewModel(ClassTable.Data.Course course) {
        initData(course);
    }

    private void initData(ClassTable.Data.Course course){
        String name = course.coursename;
        int color = course.coursecolor;
        if (!name.equals("无")){
            this.courseName.set(name);
            this.location.set("@"+ CourseHelper.getTodayLocation(course.arrange));
        }else {
            this.courseName.set(name);
        }
        this.cardColor.set(ResourceHelper.getColor(color));
        if (color == com.twtstudio.retrox.schedule.R.color.myWindowBackgroundGray){
            textColor.set(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.schedule_gray));
        }else {
            textColor.set(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.white_color));
        }

        teacherName.set(course.teacher);
        credit.set(course.credit+"学分");
        timePeriod.set("第"+courseHelper.getTodayStart(course.arrange)+"-"+courseHelper.getTodayEnd(course.arrange)+"节");
    }
}
