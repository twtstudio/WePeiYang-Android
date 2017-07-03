package com.twtstudio.retrox.schedule.view;

import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.BR;
import com.twtstudio.retrox.schedule.R;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.model.CourseHelper;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by zhangyulong on 7/3/17.
 */

public class ScheduleNewViewModel {
    private RxAppCompatActivity rxActivity;

    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();

    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(SelectedDateInfoViewModel.class, BR.viewModel, R.layout.item_selected_date)
            .build();

    public final CourseHelper courseHelper = new CourseHelper();

    public ScheduleNewViewModel(RxAppCompatActivity rxActivity,CalendarDay calendarDay) {
        this.rxActivity = rxActivity;
        setData(calendarDay);
    }


    public void setData(CalendarDay calendarDay){
        items.clear();
        items.add(new SelectedDateInfoViewModel(calendarDay));
    }

}
