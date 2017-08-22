package com.twtstudio.service.classroom.view;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.*;
import com.kelin.mvvmlight.base.ViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.service.classroom.BR;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.database.DBManager;
import com.twtstudio.service.classroom.database.RoomCollection;
import com.twtstudio.service.classroom.model.ClassRoomProvider;
import com.twtstudio.service.classroom.model.CollectedRoom2;

import java.util.List;

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
            .put(CollectionItemViewModel.class, BR.viewModel, R.layout.list_item)
            .build();
    List<RoomCollection> roomCollections;
    CollectedPageViewModel(RxAppCompatActivity rxActivity){
//        roomCollections=dbManager.queryRoomCollectionList();
        this.rxActivity=rxActivity;
    }
    public void getCollections(){
//        for (RoomCollection roomCollection:roomCollections)
//            items.add(new CollectionItemViewModel(rxActivity,roomCollection.toFreeRoom()));
    }
}
