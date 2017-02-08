package com.twtstudio.retrox.schedule.view;

import android.content.Context;
import android.content.Intent;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.schedule.ScheduleActivity;

/**
 * Created by retrox on 2017/2/8.
 */

public class ToScheduleActViewModel implements ViewModel{

    private Context mContext;

    public ToScheduleActViewModel(Context mContext) {
        this.mContext = mContext;
    }

    public final ReplyCommand clickCmd = new ReplyCommand(()->{
        Intent intent = new Intent(mContext, ScheduleActivity.class);
        mContext.startActivity(intent);
    });
}
