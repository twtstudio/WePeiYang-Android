package com.twtstudio.retrox.schedule;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.databinding.ActivityScheduleNewBinding;
import com.twtstudio.retrox.schedule.view.ScheduleNewViewModel;
import com.twtstudio.retrox.schedule.view.SelectedDateInfoViewModel;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleNewActivity extends RxAppCompatActivity {


    MaterialCalendarView calendarView;
    ScheduleNewViewModel viewModel;
    ActivityScheduleNewBinding mbinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_new);
        viewModel= new ScheduleNewViewModel(this,CalendarDay.today());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.schedule_primary_color));
        }
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_new);
        mbinding.setViewModel(viewModel);
        setSupportActionBar(mbinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCalendar();


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
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.setTopbarVisible(false);
        List<CalendarDay> calendarDays = new ArrayList<>();
        calendarDays.add(CalendarDay.today());
        EventDecorator eventDecorator = new EventDecorator(R.color.schedule_red, calendarDays);
        calendarView.setSelectedDate(CalendarDay.today());
        mbinding.tvDate.setText(CalendarDay.today().getYear() + "年" + (CalendarDay.today().getMonth() + 1) + "月");
        calendarView.setOnMonthChangedListener((calendarView, calendarDay) -> {
            calendarView.setSelectedDate(calendarDay);
            if (calendarView.getSelectedDate() != null)
                mbinding.tvDate.setText(calendarView.getSelectedDate().getYear() + "年" + (calendarView.getSelectedDate().getMonth() + 1) + "月");
        });
        calendarView.setOnDateChangedListener((calendarView, calendarDay, selected) -> {
            if (calendarView.getSelectedDate() != null)
                viewModel.setData(calendarDay);
            //tvDate.setText(calendarView.getSelectedDate().toString());
        });
        calendarView.addDecorator(eventDecorator);
        calendarView.invalidateDecorators();
    }
}
