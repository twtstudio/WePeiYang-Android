package com.twtstudio.service.classroom.view;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.*;
import com.kelin.mvvmlight.base.ViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.service.classroom.BR;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.model.ClassRoomProvider;
import com.twtstudio.service.classroom.model.CollectedRoom2;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by zhangyulong on 7/14/17.
 */

public class CollectedPageViewModel {
    private RxAppCompatActivity rxActivity;
    public final ObservableField<Boolean> error=new ObservableField<>(false);
    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(ItemViewModel.class, BR.viewModel, R.layout.list_item)
            .build();
    CollectedPageViewModel(RxAppCompatActivity rxActivity){
        this.rxActivity=rxActivity;
    }
    public void getCollected(String token,int week){
        ClassRoomProvider.init(rxActivity).registerAction((freeRoom2)->{
            if(freeRoom2.getData().isEmpty()) error.set(true);
        }).getAllCollectedClassroom(token,week);
    }
}
