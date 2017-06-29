package com.twtstudio.retrox.gpa.view;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.annimon.stream.Stream;
import com.kelin.mvvmlight.base.ViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.gpa.BR;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.R;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;
/**
 * Created by tjliqy on 2017/6/2.
 */

public class EvaluateListViewModel implements ViewModel {
    private RxAppCompatActivity mRxActivity;
    public final ObservableList<ViewModel> items = new ObservableArrayList<>();

    public EvaluateListViewModel(RxAppCompatActivity rxActivity) {
        mRxActivity = rxActivity;
    }


    public final ObservableList<ViewModel> mViewModels = new ObservableArrayList<>();
    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.gpa_item_evaluate);

    public void setCourses(ArrayList<GpaBean.Term.Course> unEvaluatedCourses){
        Stream.of(unEvaluatedCourses)
                .map(course -> new EvaluateCourseViewModel(course,mRxActivity))
                .forEach(items::add);
    }
}
