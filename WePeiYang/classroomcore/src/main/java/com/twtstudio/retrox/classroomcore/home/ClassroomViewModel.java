package com.twtstudio.retrox.classroomcore.home;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.classroomcore.BR;
import com.twtstudio.retrox.classroomcore.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.tatarka.bindingcollectionadapter.ItemView;


/**
 * Created by retrox on 2017/2/22.
 */

public class ClassroomViewModel implements ViewModel{

    public final ObservableField<String> message = new ObservableField<>();

    public final ObservableBoolean isProgressing = new ObservableBoolean(false);

    public final ObservableArrayList<BuildingItemViewModel> viewModels = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_common_classroom_building);

    public final ReplyCommand refreshClick = new ReplyCommand(()->{
        setQueryTime();
        for (BuildingItemViewModel viewModel : viewModels) {
            viewModel.getData();
        }
    });

    private void setQueryTime(){
        SimpleDateFormat dateFormate = new SimpleDateFormat("HH:mm", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String s = dateFormate.format(date);
        message.set("查询时间: "+s);
    }

    public ClassroomViewModel(Context context) {
        setQueryTime();
        viewModels.add(new BuildingItemViewModel(45,context));
        viewModels.add(new BuildingItemViewModel(46,context));
        viewModels.add(new BuildingItemViewModel(55,context));
    }
}
