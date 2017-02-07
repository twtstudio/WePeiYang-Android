package com.twtstudio.retrox.wepeiyangrd.home.common.schedule;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.annimon.stream.Stream;
import com.kelin.mvvmlight.base.ViewModel;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.BR;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.model.CourseHelper;
import com.twtstudio.retrox.wepeiyangrd.R;


import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by retrox on 2017/2/7.
 */

public class ScheduleViewModel implements ViewModel {

    private RxAppCompatActivity rxAppCompatActivity;

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_common_course);

    public ScheduleViewModel(RxAppCompatActivity rxActivity) {
        this.rxAppCompatActivity = rxActivity;
        getData();
    }

    public void getData(){
        ClassTableProvider.init(rxAppCompatActivity)
                .registerAction(this::handleClassTable)
                .getData();
    }

    private void handleClassTable(ClassTable classTable){
        List<ClassTable.Data.Course> courseList = new CourseHelper().getTodayCourses(classTable,true);
        for (ClassTable.Data.Course course : courseList) {
            items.add(new CourseBriefViewModel(course));
        }
        Logger.d(items);
//        Stream.of(new CourseHelper().getTodayCourses(classTable))
//                .peek(course -> Logger.d(course.coursename))
//                .map(CourseBriefViewModel::getInstance)
//                .peek(items::add);
//        items.size();
    }
}
