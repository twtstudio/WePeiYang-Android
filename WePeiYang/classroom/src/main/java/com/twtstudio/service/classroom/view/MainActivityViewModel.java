package com.twtstudio.service.classroom.view;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

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
    public final ObservableField<String> condition1 = new ObservableField<>("教学楼");
    public final ObservableField<String> condition2 = new ObservableField<>("时间段");
    public final ObservableField<Boolean> isError = new ObservableField<>();
    public final ObservableField<Boolean> isLoading = new ObservableField<>();
    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(ItemViewModel.class, BR.viewModel, R.layout.list_item)
            .build();
    public static int building, week, time;
    String token;
    boolean update;

    MainActivityViewModel(RxAppCompatActivity rxAppCompatActivity) {
        this.rxActivity = rxAppCompatActivity;
        building = week = time = 0;
//        iniData(buiding,week,time,token);
    }

    public void iniData(int building, int week, int time, String token, boolean update) {
        this.isError.set(false);
        this.isLoading.set(true);
        this.building = building;
        this.week = week;
        this.time = time;
        this.token = token;
        if (update)
            items.clear();
        ClassRoomProvider.init(rxActivity)
                .registerAction(this::processData)
                .getFreeClassroom(building, week, time, token);
    }

    private void processData(FreeRoom2 freeRoom2) {
        if (freeRoom2 != null)
            if (freeRoom2.getErrorcode() == 1)
                isError.set(true);
        isLoading.set(false);
        if (freeRoom2.getData() != null)
            for (FreeRoom2.FreeRoom freeRoom : freeRoom2.getData())
                items.add(new ItemViewModel(rxActivity, freeRoom, this));
    }

    public void setCollected(String building) {
        ClassRoomProvider.init(rxActivity).collect(building, token);
    }

    public void cancelCollected(String building) {
        ClassRoomProvider.init(rxActivity).cancelCollect(token, building);
    }

    public void getCollected() {
        ClassRoomProvider.init(rxActivity).getAllCollectedClassroom(token, week);
    }

}