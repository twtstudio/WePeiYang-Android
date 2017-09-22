package com.twt.service.home.tools;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.net.Uri;

import com.bdpqchen.yellowpagesmodule.yellowpages.activity.HomeActivity;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.bike.bike.ui.main.BikeActivity;
import com.twtstudio.retrox.bike.read.home.BookHomeActivity;
import com.twtstudio.retrox.gpa.view.GpaActivity;
import com.twtstudio.retrox.schedule.ScheduleActivity;
import com.twt.service.BR;
import com.twt.service.R;
import com.twtstudio.tjliqy.party.ui.home.PartyActivity;
import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallActivity;

import me.tatarka.bindingcollectionadapter.ItemView;


/**
 * Created by retrox on 2017/1/15.
 */

public class ToolsFragViewModel implements ViewModel {
    private Context mContext;

    public final ObservableArrayList<ViewModel> itemList = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_tool);

    public final ReplyCommand feedbackClick = new ReplyCommand(this::feedback);

    public ToolsFragViewModel(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        itemList.add(new ToolItemViewModel(mContext, R.drawable.ic_main_schedule, "课程表", ScheduleActivity.class));
        itemList.add(new ToolItemViewModel(mContext, R.drawable.ic_main_gpa, "GPA", GpaActivity.class));
        itemList.add(new ToolItemViewModel(mContext, R.drawable.ic_main_bike, "自行车", BikeActivity.class));
        itemList.add(new ToolItemViewModel(mContext, R.drawable.ic_main_party, "党建", PartyActivity.class));
        itemList.add(new ToolItemViewModel(mContext, R.drawable.ic_main_read, "阅读", BookHomeActivity.class));
        itemList.add(new ToolItemViewModel(mContext, R.drawable.ic_main_fellow_search, "老乡查询", com.example.caokun.fellowsearch.view.MainActivity.class));
        itemList.add(new ToolItemViewModel(mContext, R.drawable.ic_main_yellowpage, "黄页", HomeActivity.class));
        itemList.add(new ToolItemViewModel(mContext, R.drawable.ic_main_classroom_query, "自习室", com.twtstudio.service.classroom.view.MainActivity.class));
        itemList.add(new ToolItemViewModel(mContext, R.drawable.ic_main_classroom_query, "失物招领", WaterfallActivity.class));
    }

    private void feedback() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://support.twtstudio.com/category/6/%E7%A7%BB%E5%8A%A8%E5%AE%A2%E6%88%B7%E7%AB%AF"));
//                    intent.createChooser(intent,"选择浏览器");
        mContext.startActivity(intent);
    }
}
