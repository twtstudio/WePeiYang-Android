package com.twtstudio.service.classroom.view;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.*;
import com.twtstudio.service.classroom.model.FreeRoom2;

/**
 * Created by zhangyulong on 7/11/17.
 */

public class ItemViewModel implements com.kelin.mvvmlight.base.ViewModel {
    public final ObservableField<String> classroom=new ObservableField<>();
    public final ObservableField<String> campus=new ObservableField<>();
    public final ObservableField<Boolean> isVisible1=new ObservableField<>();
    public final ObservableField<Boolean> isVisible2=new ObservableField<>();
    public final ObservableField<Boolean> isVisible3=new ObservableField<>();
    public final ObservableField<Boolean> isVisible4=new ObservableField<>();
     ItemViewModel(FreeRoom2.FreeRoom freeRoom) {
        initData(freeRoom);
    }
    private void initData(FreeRoom2.FreeRoom freeRoom){
        classroom.set(freeRoom.getRoom());
    }
}
