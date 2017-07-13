package com.twtstudio.service.classroom.view;

import android.databinding.ObservableField;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.model.FreeRoom2;

/**
 * Created by zhangyulong on 7/11/17.
 */

public class ItemViewModel implements com.kelin.mvvmlight.base.ViewModel {
    public final ObservableField<String> classroom = new ObservableField<>();
    public final ObservableField<String> campus = new ObservableField<>();
    public final ObservableField<Integer> resid1 = new ObservableField<>();
    public final ObservableField<Integer> resid2 = new ObservableField<>();
    public final ObservableField<Integer> resid3 = new ObservableField<>();
    public final ObservableField<Integer> resid4 = new ObservableField<>();
    public final ObservableField<Boolean> isVisible5 = new ObservableField<>(false);
    public final ObservableField<Boolean> isVisible2 = new ObservableField<>(false);
    public final ObservableField<Boolean> isVisible3 = new ObservableField<>(false);
    public final ObservableField<Boolean> isVisible4 = new ObservableField<>(false);
    RxAppCompatActivity rxAppCompatActivity;

    ItemViewModel(RxAppCompatActivity rxAppCompatActivity, FreeRoom2.FreeRoom freeRoom) {
        this.rxAppCompatActivity = rxAppCompatActivity;
        initData(freeRoom);
    }

    private void initData(FreeRoom2.FreeRoom freeRoom) {
//        Uri uri1=Uri.parse("android.resource://" + rxAppCompatActivity.getApplicationContext().getPackageName() + "/" +R.drawable.classroom_tag_empty);
        if (freeRoom.getState().equals("空闲"))
            resid1.set(R.drawable.classroom_tag_empty);
        else if (freeRoom.getState().equals("上课中"))
            resid1.set(R.drawable.classroom_tag_inclass);
        if (freeRoom.getState().equals("上课中"))
            resid1.set(R.drawable.classroom_tag_inclass);
        if (freeRoom.getState().equals("即将上课"))
            resid1.set(R.drawable.classroom_tag_willhaveclass);
        if (freeRoom.getState().equals("即将下课"))
            resid1.set(R.drawable.classroom_tag_willbeempty);

        if(freeRoom.getHeating()) isVisible4.set(true);
        if(freeRoom.getWater_dispenser()) isVisible5.set(true);
        if(freeRoom.getPower_pack()) isVisible3.set(true);
        classroom.set(freeRoom.getRoom());

    }
}
