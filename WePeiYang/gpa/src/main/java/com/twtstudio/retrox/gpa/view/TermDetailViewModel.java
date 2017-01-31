package com.twtstudio.retrox.gpa.view;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.gpa.BR;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.R;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by retrox on 2017/1/28.
 */

public class TermDetailViewModel implements ViewModel {

    public final ObservableField<GpaBean.Term.TermStat> observableTermStat = new ObservableField<>();

    /**
     * bind a LinearLayout gg
     */

    public final ObservableArrayList<GpaBean.Term.Course> mObservableCourseList = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.gpa_item_term_course);

    public final ObservableArrayList<ViewModel> courseViewModels = new ObservableArrayList<>();

    public TermDetailViewModel(GpaBean.Term term) {
        observableTermStat.set(term.stat);
        mObservableCourseList.addAll(term.data);
        initCourseData();

        mObservableCourseList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<GpaBean.Term.Course>>() {
            @Override
            public void onChanged(ObservableList<GpaBean.Term.Course> courses) {
                initCourseData();
            }

            @Override
            public void onItemRangeChanged(ObservableList<GpaBean.Term.Course> courses, int i, int i1) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<GpaBean.Term.Course> courses, int i, int i1) {

            }

            @Override
            public void onItemRangeMoved(ObservableList<GpaBean.Term.Course> courses, int i, int i1, int i2) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<GpaBean.Term.Course> courses, int i, int i1) {

            }
        });
    }

    private void initCourseData() {
        courseViewModels.clear();
        courseViewModels.addAll(Stream.of(mObservableCourseList)
                .map(TermCourseViewModel::newInstance)
                .collect(Collectors.toList()));
    }



}
