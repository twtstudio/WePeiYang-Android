package com.twtstudio.retrox.schedule.view;

import android.arch.lifecycle.MutableLiveData;

import com.twtstudio.retrox.schedule.ResourceHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;



/**
 * Created by retrox on 2017/2/8.
 */

public class CourseEmptyViewModel implements ViewModel {
    public final MutableLiveData<String> courseName = new MutableLiveData<>();

    public final MutableLiveData<Integer> cardColor = new MutableLiveData<>();

    public final MutableLiveData<Integer> textColor = new MutableLiveData<>();

    public CourseEmptyViewModel(ClassTable.Data.Course course) {
        courseName.setValue(course.coursename);
        this.cardColor.setValue(ResourceHelper.getColor(course.coursecolor));
        if (course.coursecolor == com.twtstudio.retrox.schedule.R.color.myWindowBackgroundGray){
            textColor.setValue(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.schedule_gray));
        }else {
            textColor.setValue(ResourceHelper.getColor(com.twtstudio.retrox.schedule.R.color.white_color));
        }

    }
}
