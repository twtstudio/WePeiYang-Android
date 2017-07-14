package com.twtstudio.retrox.gpa.view;

import android.content.Intent;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.gpa.GpaBean;

/**
 * Created by retrox on 2017/1/29.
 */

public class EvaluateCourseViewModel implements ViewModel {

    public final ObservableField<String> name = new ObservableField<>();

    public final ObservableField<String> score = new ObservableField<>();

    public final ObservableField<String> credit = new ObservableField<>();

    public final ReplyCommand goEvaluate = new ReplyCommand(this::onClick);

    private GpaBean.Term.Course mCourse;

    private RxAppCompatActivity mRxActivity;

    public EvaluateCourseViewModel(GpaBean.Term.Course course, RxAppCompatActivity activity) {
        mCourse = course;
        mRxActivity = activity;
        name.set(course.name);
        score.set(String.valueOf(course.score));
        credit.set(String.valueOf(course.credit));
    }

    public static EvaluateCourseViewModel newInstance(GpaBean.Term.Course course,RxAppCompatActivity activity) {
        return new EvaluateCourseViewModel(course,activity);
    }

    public GpaBean.Term.Course getCourse(){
        return mCourse;
    }
    public void onClick() {
        Intent intent = new Intent(mRxActivity,EvaluateDetailActivity.class);
        intent.putExtra("key",mCourse);
        mRxActivity.startActivity(intent);
    }
}
