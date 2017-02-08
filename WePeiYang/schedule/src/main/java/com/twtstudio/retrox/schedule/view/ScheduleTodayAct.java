package com.twtstudio.retrox.schedule.view;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.R;
import com.twtstudio.retrox.schedule.databinding.ActivityScheduleTodayDetailBinding;

/**
 * Created by retrox on 2017/2/8.
 */

public class ScheduleTodayAct extends RxAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        ActivityScheduleTodayDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_today_detail);
        binding.setViewModel(new ScheduleTodayViewModel(this));
    }
}
