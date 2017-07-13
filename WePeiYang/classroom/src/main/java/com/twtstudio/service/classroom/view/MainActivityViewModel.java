package com.twtstudio.service.classroom.view;

import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.service.classroom.BR;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.model.ClassRoomProvider;
import com.twtstudio.service.classroom.model.FreeRoom2;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by zhangyulong on 7/11/17.
 */

public class MainActivityViewModel {
    private RxAppCompatActivity rxActivity;
    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(ItemViewModel.class, BR.viewModel, R.layout.list_item)
            .build();

    MainActivityViewModel(RxAppCompatActivity rxAppCompatActivity) {
        this.rxActivity = rxAppCompatActivity;
//        iniData(buiding,week,time,token);
    }

    public void iniData(int buiding, int week, int time, String token) {
        ClassRoomProvider.init(rxActivity)
                .registerAction(this::processData)
                .getFreeClassroom(buiding, week, time, token);
    }

    private void processData(FreeRoom2 freeRoom2) {
        items.clear();
        for (FreeRoom2.FreeRoom freeRoom : freeRoom2.getData())
            items.add(new ItemViewModel(rxActivity, freeRoom));
    }
}
