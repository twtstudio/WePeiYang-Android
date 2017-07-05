package com.twtstudio.retrox.schedule;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.databinding.ActivityScheduleNewBinding;
import com.twtstudio.retrox.schedule.view.ScheduleNewViewModel;

import java.util.ArrayList;
import java.util.List;

public class ScheduleNewActivity extends RxAppCompatActivity {


    ScheduleNewViewModel viewModel;
    ActivityScheduleNewBinding mbinding;
    private boolean isWeekMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_new);
        viewModel = new ScheduleNewViewModel(this, CalendarDay.today());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.schedule_primary_color));
        }
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_new);
        mbinding.setViewModel(viewModel);
        setSupportActionBar(mbinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCalendar();

        mbinding.list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEvent(v, event);
            }
        });
        mbinding.scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEvent(v, event);
            }
            //mbinding.calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.schedule_switch) {
            //switch
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
            finish();


        }
        return super.onOptionsItemSelected(item);
    }

    void initCalendar() {
        mbinding.calendarView.state().edit().isCacheCalendarPositionEnabled(true).commit();
        mbinding.calendarView.setTopbarVisible(false);
        List<CalendarDay> calendarDays = new ArrayList<>();
        calendarDays.add(CalendarDay.today());
        EventDecorator eventDecorator = new EventDecorator(R.color.schedule_red, calendarDays);
        mbinding.calendarView.setSelectedDate(CalendarDay.today());
        mbinding.tvDate.setText(CalendarDay.today().getYear() + "年" + (CalendarDay.today().getMonth() + 1) + "月");
        mbinding.calendarView.setOnMonthChangedListener((calendarView, calendarDay) -> {
            if (!(mbinding.calendarView.getSelectedDate().getMonth() == calendarDay.getMonth())) {
                mbinding.calendarView.setSelectedDate(calendarDay);
                viewModel.initData(calendarDay);
            }

            if (calendarView.getSelectedDate() != null)
                mbinding.tvDate.setText(calendarView.getSelectedDate().getYear() + "年" + (calendarView.getSelectedDate().getMonth() + 1) + "月");
        });
        mbinding.calendarView.setOnDateChangedListener((calendarView, calendarDay, selected) -> {
            viewModel.initData(calendarDay);
            //tvDate.setText(calendarView.getSelectedDate().toString());
        });
        mbinding.calendarView.addDecorator(eventDecorator);
        mbinding.calendarView.invalidateDecorators();
    }

    void setScaleAnim(boolean isUp) {
        if (isUp) {
            Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            mbinding.calendarView.startAnimation(mAnimation);
        } else {
            Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_down);
            mbinding.calendarView.startAnimation(mAnimation);
        }
    }

    boolean onTouchEvent(View v, MotionEvent event) {
        float mPosX, mPosY, mCurPosX, mCurPosY;
        mPosX = mPosY = mCurPosX = mCurPosY = 0;
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                mPosY = event.getY();
                mbinding.scroll.setEnabled(false);
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosX = event.getX();
                mCurPosY = event.getY();
                if (mCurPosY - mPosY > 0
                        && (Math.abs(mCurPosY - mPosY) > 20)) {
                    //向下滑動
                    if (isWeekMode) {
                        setScaleAnim(false);
                        mbinding.calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                        isWeekMode = false;
                    }
                    return true;
                } else if (mCurPosY - mPosY < 0
                        && (Math.abs(mCurPosY - mPosY) > 20)) {
                    if (!isWeekMode) {
                        setScaleAnim(true);
                        //向上滑动
                        CalendarDay calendarDay = mbinding.calendarView.getSelectedDate();
                        mbinding.calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                        mbinding.calendarView.setSelectedDate(calendarDay);
                        viewModel.initData(calendarDay);
                        this.isWeekMode = true;
                        mbinding.scroll.setEnabled(true);
                    }
                    else
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}
