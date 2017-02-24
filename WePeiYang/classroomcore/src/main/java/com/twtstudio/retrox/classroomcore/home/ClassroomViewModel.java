package com.twtstudio.retrox.classroomcore.home;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.classroomcore.BR;
import com.twtstudio.retrox.classroomcore.R;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by retrox on 2017/2/22.
 */

public class ClassroomViewModel implements ViewModel{

    public final ObservableField<String> message = new ObservableField<>("查询完成 18:10");

    public final ObservableBoolean isProgressing = new ObservableBoolean(false);

    public final ObservableArrayList<BuildingItemViewModel> viewModels = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_common_classroom_building);

    public ClassroomViewModel(Context context) {
        viewModels.add(new BuildingItemViewModel(45,context));
        viewModels.add(new BuildingItemViewModel(46,context));
        viewModels.add(new BuildingItemViewModel(55,context));
    }
}
