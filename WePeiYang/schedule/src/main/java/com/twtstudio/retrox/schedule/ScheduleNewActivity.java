package com.twtstudio.retrox.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelin.mvvmlight.base.ViewModel;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.utils.PrefUtil;
import com.twtstudio.retrox.schedule.view.ScheduleNewAdapter;
import com.twtstudio.retrox.schedule.view.ScheduleNewViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScheduleNewActivity extends RxAppCompatActivity {


    ScheduleNewViewModel viewModel;
    CoordinatorLayout content;
    private MonthPager monthPager;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private int padding;
    private ScheduleNewAdapter listAdapter;
    private TextView tvDate;
    private RecyclerView rvCourseList;
    private LinearLayout linear;
    private SwipeRefreshLayout refresh;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_new);
        //判断默认显示的Activity
        if (!PrefUtil.getIsNewSchedule()) {
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
            finish();
        }
        tvDate=(TextView) findViewById(R.id.tv_date);
        monthPager=(MonthPager) findViewById(R.id.calendar_view);
        rvCourseList=(RecyclerView) findViewById(R.id.list); 
        linear=(LinearLayout) findViewById(R.id.linear); 
        refresh=(SwipeRefreshLayout) findViewById(R.id.refresh); 
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        listAdapter=new ScheduleNewAdapter(this,this);
        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator) ;
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewModel = new ScheduleNewViewModel(this, CalendarDay.today());
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.schedule_primary_color));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvCourseList.setLayoutManager(linearLayoutManager);
        rvCourseList.setAdapter(listAdapter);
        rvCourseList.setHasFixedSize(true);
        refresh.setProgressViewOffset(true, 120, 150);
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) monthPager.getLayoutParams();
        padding = metrics.widthPixels / 20;
        params.setMargins(padding, 0, padding, 0);
        monthPager.setLayoutParams(params);
        linear.setPadding(padding, 0, padding, 0);
        monthPager.setOnTouchListener((v, event) -> {
            refresh.setEnabled(false);
            return false;

        });
        int[] location1 = new int[2];
        refresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rvCourseList.dispatchTouchEvent(event);
                return false;
            }
        });
        rvCourseList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        rvCourseList.getLocationInWindow(location1);
//                        int i = Utils.dpi2px(context, 100);
//                        Log.d("scroll", Integer.toString(location1[1]));
                        if (location1[1] > metrics.heightPixels / 2) {
                            refresh.setEnabled(true);
                        }
                        else
                            refresh.setEnabled(false);

                }
                return false;
            }
        });
        coordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        rvCourseList.getLocationInWindow(location1);
//                        int i = Utils.dpi2px(context, 100);
//                        Log.d("scroll", Integer.toString(location1[1]));
                        if (location1[1] > metrics.heightPixels / 2) {
                            refresh.setEnabled(true);
                        }
                        else
                            refresh.setEnabled(false);

                }
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
            PrefUtil.setIsNewSchedule(false);
            finish();


        }
        return super.onOptionsItemSelected(item);
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager();
            initiated = true;

        }
    }


    private void initCurrentDate() {
        currentDate = new CalendarDate();
    }

    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.schedule_custom_day, this);
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
        tvDate.setText(date.getYear() + "年" + date.getMonth() + "月");
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
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) instanceof Calendar) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    Log.d("position", Integer.toString(position));
                    refreshClickDate(currentDate);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        refreshClickDate(today);
    }


    private void onRfreshData() {
        ClassTableProvider.init(this).registerAction(this::onRfreshing)
                .getData(true);


    }

    private void onRfreshing(ClassTable classTable) {
        refreshClickDate(currentDate);
        refresh.setRefreshing(false);
    }


    public void updateAdapter(List<ViewModel> viewModels){
        listAdapter.refreshData(viewModels);
    }
}
