package com.twtstudio.retrox.schedule.view;

import android.arch.lifecycle.MutableLiveData;


import com.twtstudio.retrox.schedule.ResourceHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;

/**
 * Created by retrox on 2017/2/7.
 */

public class CourseDetailViewModel implements ViewModel {

    public final MutableLiveData<String> courseName = new MutableLiveData<>();

    public final MutableLiveData<Integer> cardColor = new MutableLiveData<>();

    public final MutableLiveData<Integer> textColor = new MutableLiveData<>();

    public final MutableLiveData<String> teacherName = new MutableLiveData<>();

    public final MutableLiveData<String> credit = new MutableLiveData<>();

    public final MutableLiveData<String> location = new MutableLiveData<>();

    private final CourseHelper courseHelper = new CourseHelper();

    public final MutableLiveData<String> timePeriod = new MutableLiveData<>();

    public CourseDetailViewModel(ClassTable.Data.Course course) {
        initData(course);
    }

    private void initData(ClassTable.Data.Course course){
        String name = course.coursename;
        int color = course.coursecolor;
        if (!name.equals("无")){
            this.courseName.setValue(name);
            this.location.setValue("@"+ CourseHelper.getTodayLocation(course.arrange));
        }else {
            this.courseName.setValue(name);
        }
        this.cardColor.setValue(ResourceHelper.getColor(color));
        if (color == com.twtstudio.retrox.schedule.R.color.myWindowBackgroundGray){
            textColor.setValue(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.schedule_gray));
        }else {
            textColor.setValue(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.white_color));
        }

        teacherName.setValue(course.teacher);
        credit.setValue(course.credit+"学分");
        timePeriod.setValue("第"+courseHelper.getTodayStart(course.arrange)+"-"+courseHelper.getTodayEnd(course.arrange)+"节");
    }
}
