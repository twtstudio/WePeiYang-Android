package com.twt.service.ui.tools;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.rxsrc.bike.bikeAuth.BikeAuthActivity;
import com.twt.service.rxsrc.bike.ui.main.BikeActivity;
import com.twt.service.party.ui.home.PartyActivity;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.bus.BusActivity;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.date.DatingActivity;
import com.twt.service.ui.gpa.GpaActivity;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.schedule.ScheduleActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by tjliqy on 2016/8/23.
 */
public class ToolsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.btn_schedule)
    LinearLayout btnSchedule;
    @InjectView(R.id.btn_gpa_query)
    LinearLayout btnGpaQuery;
    @InjectView(R.id.btn_bike)
    LinearLayout btnBike;
    @InjectView(R.id.btn_party)
    LinearLayout btnParty;
    @InjectView(R.id.btn_dating)
    LinearLayout btnDating;
    @InjectView(R.id.btn_bus)
    LinearLayout btnBus;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ToolsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        ButterKnife.inject(this);

        toolbar.setTitle("工具");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_primary));
        }
    }


    @OnClick({R.id.btn_schedule, R.id.btn_gpa_query, R.id.btn_bike, R.id.btn_party, R.id.btn_dating, R.id.btn_bus})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_schedule:
                if (PrefUtils.isLogin()) {
                    ScheduleActivity.actionStart(this);
                } else {
                    LoginActivity.actionStart(this, NextActivity.Schedule);
                }
                break;
            case R.id.btn_gpa_query:
                if (PrefUtils.isLogin()) {
                    GpaActivity.actionStart(this);
                } else {
                    LoginActivity.actionStart(this, NextActivity.Gpa);
                }
                break;
            case R.id.btn_bus:
                if (PrefUtils.isLogin()) {
                    BusActivity.actionStart(this);
                } else {
                    LoginActivity.actionStart(this, NextActivity.Bus);
                }
                break;
            case R.id.btn_dating:
                if (PrefUtils.isLogin()) {
                    DatingActivity.actionStart(this);
                } else {
                    LoginActivity.actionStart(this, NextActivity.Dating);
                }
                break;
            case R.id.btn_party:
                if (PrefUtils.isLogin()) {
                    PartyActivity.actionStart(this);
                } else {
                    LoginActivity.actionStart(this, NextActivity.Party);
                }
                break;
            case R.id.btn_bike:
                if (android.os.Build.CPU_ABI.equals("x86")) {
                    Toast.makeText(ToolsActivity.this, "因为某些硬件问题，自行车功能无法添加对x86架构手机的支持", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (PrefUtils.getBikeIsBindState()) {
                    BikeActivity.actionStart(ToolsActivity.this);
                } else if (PrefUtils.isLogin()) {
                    startActivity(new Intent(ToolsActivity.this, BikeAuthActivity.class));
                } else {
                    LoginActivity.actionStart(ToolsActivity.this, NextActivity.Bike);
                }
                break;
        }
    }
}
