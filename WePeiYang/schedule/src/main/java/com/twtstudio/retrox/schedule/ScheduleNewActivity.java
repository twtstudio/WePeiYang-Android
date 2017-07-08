package com.twtstudio.retrox.schedule;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.schedule.databinding.ActivityScheduleNewBinding;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.view.ScheduleNewViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleNewActivity extends RxAppCompatActivity {


    ScheduleNewViewModel viewModel;
    ActivityScheduleNewBinding mbinding;
    RecyclerView rvToDoList;
    CoordinatorLayout content;
    SwipeRefreshLayout refresh;
    private MonthPager monthPager;
    private boolean isWeekMode = false;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_new);
        //判断默认显示的Activity
        if (!CommonPrefUtil.getIsNewSchedule()) {
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
            finish();
        }
        viewModel = new ScheduleNewViewModel(this, CalendarDay.today());
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.schedule_primary_color));
        }
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_new);
        mbinding.setViewModel(viewModel);
        monthPager = mbinding.calendarView;
        refresh = mbinding.refresh;
        setSupportActionBar(mbinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvToDoList = mbinding.list;
        rvToDoList.setHasFixedSize(true);
//        initCalendar();
//
//
//        mbinding.scroll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return onTouchEvent(v, event);
//            }
//            //mbinding.calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
//        });
//        mbinding.list.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return onTouchEvent(v, event);
//            }
//        });
        refresh.setProgressViewOffset(true, 120, 150);
        monthPager.setOnTouchListener((v, event) -> {
                    refresh.setEnabled(false);
                    return false;
        });
        rvToDoList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int[] location1 = new int[2];
                rvToDoList.getLocationInWindow(location1);
                int i = Utils.dpi2px(context, 100);
                Log.d("scroll", Integer.toString(i));
                if (location1[1] < Utils.dpi2px(context, 350)) {
                    refresh.setEnabled(false);
                    return false;
                }
                refresh.setEnabled(true);

                return false;
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRfreshData();
            }
        });
        initCurrentDate();
        initCalendarView();
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
            CommonPrefUtil.setIsNewSchedule(false);
            finish();


        }
        return super.onOptionsItemSelected(item);
    }

//    void initCalendar() {
//        mbinding.calendarView.state().edit().isCacheCalendarPositionEnabled(true).commit();
//        mbinding.calendarView.setTopbarVisible(false);
//        List<CalendarDay> calendarDays = new ArrayList<>();
//        calendarDays.add(CalendarDay.today());
//        EventDecorator eventDecorator = new EventDecorator(R.color.schedule_red, calendarDays);
//        mbinding.calendarView.setSelectedDate(CalendarDay.today());
//        mbinding.tvDate.setText(CalendarDay.today().getYear() + "年" + (CalendarDay.today().getMonth() + 1) + "月");
//        mbinding.calendarView.setOnMonthChangedListener((calendarView, calendarDay) -> {
//            if (!(mbinding.calendarView.getSelectedDate().getMonth() == calendarDay.getMonth())) {
//                mbinding.calendarView.setSelectedDate(calendarDay);
//                viewModel.initData(calendarDay);
//            }
//
//            if (calendarView.getSelectedDate() != null)
//                mbinding.tvDate.setText(calendarView.getSelectedDate().getYear() + "年" + (calendarView.getSelectedDate().getMonth() + 1) + "月");
//        });
//        mbinding.calendarView.setOnDateChangedListener((calendarView, calendarDay, selected) -> {
//            viewModel.initData(calendarDay);
//            //tvDate.setText(calendarView.getSelectedDate().toString());
//        });
//        mbinding.calendarView.addDecorator(eventDecorator);
//        mbinding.calendarView.invalidateDecorators();
//    }

    //    void setScaleAnim(boolean isUp) {
//        if (isUp) {
//            Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
//            mbinding.calendarView.startAnimation(mAnimation);
//        } else {
//            Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_down);
//            mbinding.calendarView.startAnimation(mAnimation);
//        }
//    }
//
//    boolean onTouchEvent(View v, MotionEvent event) {
//        float mPosX, mPosY, mCurPosX, mCurPosY;
//        mPosX = mPosY = mCurPosX = mCurPosY = 0;
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                mPosX = event.getX();
//                mPosY = event.getY();
//               // mbinding.scroll.setEnabled(false);
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                break;
//
//            case MotionEvent.ACTION_UP:
//                mCurPosX = event.getX();
//                mCurPosY = event.getY();
//                if (mCurPosY - mPosY > 0
//                        && (Math.abs(mCurPosY - mPosY) > 70)) {
//                    //向下滑動
//                    if (isWeekMode) {
//                        setScaleAnim(false);
//                        mbinding.calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
//                        isWeekMode = false;
//                        mbinding.refresh.setEnabled(true);
//
//                    }
//                    return false;
//                } else if (mCurPosY - mPosY < 0
//                        && (Math.abs(mCurPosY - mPosY) > 70)) {
//                    if (!isWeekMode) {
//                        setScaleAnim(true);
//                        //向上滑动
//                        CalendarDay calendarDay = mbinding.calendarView.getSelectedDate();
//                        mbinding.calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
//                        mbinding.calendarView.setSelectedDate(calendarDay);
//                        viewModel.initData(calendarDay);
//                        this.isWeekMode = true;
//                        mbinding.scroll.setEnabled(true);
//                        mbinding.refresh.setEnabled(false);
//                    }
//                    else
//                        return false;
//                }
//                break;
//        }
//        return false;
//    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager();
            initiated = true;

        }
    }

    private void initToolbarClickListener() {

    }

    private void initCurrentDate() {
        currentDate = new CalendarDate();
    }

    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.CalendayType.MONTH,
                customDayView);
//        initMarkData();
        initMonthPager();
    }

    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2017-8-9", "1");
        markData.put("2017-7-9", "0");
        markData.put("2017-6-9", "1");
        markData.put("2017-6-10", "0");
        calendarAdapter.setMarkData(markData);
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示上一个月 ， 1表示下一个月
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    private void refreshClickDate(CalendarDate date) {
        CalendarDay calendarDay = CalendarDay.from(date.getYear(), date.getMonth() - 1, date.getDay());
        viewModel.initData(calendarDay);
        mbinding.tvDate.setText(date.getYear() + "年" + date.getMonth() + "月");
        currentDate = date;

    }

    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars=calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) instanceof Calendar) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    Log.d("position",Integer.toString(position));
                    refreshClickDate(currentDate);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClickBackToDayBtn() {
        refreshMonthPager();
    }

    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        refreshClickDate(today);
    }

    private void refreshSelectBackground() {
        ThemeDayView themeDayView = new ThemeDayView(context, R.layout.custom_day_focus);
        calendarAdapter.setCustomDayRenderer(themeDayView);
        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(new CalendarDate());
    }

    private void onRfreshData() {
       ClassTableProvider.init(this).registerAction(this::onRfreshing)
                .getData(true);

    }

    private void onRfreshing(ClassTable classTable) {
        refresh.setRefreshing(false);
    }

}
