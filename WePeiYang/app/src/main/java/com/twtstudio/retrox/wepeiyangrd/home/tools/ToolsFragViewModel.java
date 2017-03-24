package com.twtstudio.retrox.wepeiyangrd.home.tools;

import android.content.Context;
import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.bike.bike.ui.main.BikeActivity;
import com.twtstudio.retrox.bike.read.home.BookHomeActivity;
import com.twtstudio.retrox.gpa.view.GpaActivity;
import com.twtstudio.retrox.schedule.ScheduleActivity;
import com.twtstudio.retrox.wepeiyangrd.BR;
import com.twtstudio.retrox.wepeiyangrd.MainActivity;
import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.tjliqy.party.ui.home.PartyActivity;

import me.tatarka.bindingcollectionadapter.ItemView;


/**
 * Created by retrox on 2017/1/15.
 */

public class ToolsFragViewModel implements ViewModel {
    private Context mContext;

    public final ObservableArrayList<ViewModel> itemList = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_tool);

    public ToolsFragViewModel(Context context) {
        mContext = context;
        init();
    }

    private void init(){
        itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_schedule,"课程表", ScheduleActivity.class));
        itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_gpa,"GPA", GpaActivity.class));
        itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_bike,"哲学车", BikeActivity.class));
        itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_party,"党建", PartyActivity.class));
        itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_read,"图书馆", BookHomeActivity.class));
        itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_fellow_search,"老乡查询", com.example.caokun.fellowsearch.view.MainActivity.class));
        itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_yellowpage,"黄页", HomeActivity.class));

        // TODO: 2017/1/15 修改跳转的activity
    }
}
