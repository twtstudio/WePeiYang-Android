package com.twtstudio.retrox.gpa.view;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.gpa.GpaBean;

/**
 * Created by retrox on 2017/1/29.
 */

public class TermCourseViewModel implements ViewModel {

    public final ObservableField<String> name = new ObservableField<>();

    public final ObservableField<String> score = new ObservableField<>();

    public final ObservableField<String> credit = new ObservableField<>();

    public TermCourseViewModel(GpaBean.Term.Course course) {
        name.set(course.name);
        score.set(String.valueOf(course.score));
        credit.set(String.valueOf(course.credit));
    }

    public static TermCourseViewModel newInstance(GpaBean.Term.Course course){
        return new TermCourseViewModel(course);
    }
}
