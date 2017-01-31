package com.twtstudio.retrox.wepeiyangrd.home.tools;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v7.app.AppCompatActivity;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.wepeiyangrd.base.BaseActivity;

/**
 * Created by retrox on 2017/1/15.
 */

public class ToolItemViewModel implements ViewModel {

    private Context mContext;

    private Class<? extends AppCompatActivity> targetAct;

    public final ObservableInt iconRes = new ObservableInt();

    public final ObservableField<String> title = new ObservableField<>();

    public final ReplyCommand clickCommand = new ReplyCommand(this::jump);

    public ToolItemViewModel(Context context, int iconres, String title, Class<? extends AppCompatActivity> activityClass) {
        mContext = context;
        this.iconRes.set(iconres);
        this.title.set(title);
        this.targetAct = activityClass;
    }

    private void jump(){
        Intent intent = new Intent(mContext,targetAct);
        mContext.startActivity(intent);
    }

}
