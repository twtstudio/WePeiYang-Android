package com.twt.service.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.ClassTable;
import com.twt.service.interactor.ScheduleInteractorImpl;
import com.twt.service.support.ResourceHelper;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.bind.BindActivity;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class ScheduleActivity extends BaseActivity implements ScheduleView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tv_monday)
    TextView tvMonday;
    @InjectView(R.id.tv_tuesday)
    TextView tvTuesday;
    @InjectView(R.id.tv_wendesday)
    TextView tvWendesday;
    @InjectView(R.id.tv_thursday)
    TextView tvThursday;
    @InjectView(R.id.tv_friday)
    TextView tvFriday;
    @InjectView(R.id.tv_saturday)
    TextView tvSaturday;
    @InjectView(R.id.tv_sunday)
    TextView tvSunday;
    @InjectView(R.id.pb_schedule)
    ProgressBar pbSchedule;
    @InjectView(R.id.tv_week)
    TextView tvWeek;
    @InjectView(R.id.gl_schedule)
    GridLayout glSchedule;
    private SchedulePresenterImpl presenter;
    private String currentTerm;//当前学期
    private int currentWeek;// 当前周
    private int griditemWidth;
    private int currentDay;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        EventBus.getDefault().register(this);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        // TODO: 2016/9/11 设置当前周
        currentWeek = 1;
        tvWeek.setText("第" + currentWeek + "周");


        presenter = new SchedulePresenterImpl(this, new ScheduleInteractorImpl(), this);

        glSchedule.post(new Runnable() {
            @Override
            public void run() {
                griditemWidth = glSchedule.getWidth()/15;
                presenter.loadCoursesFromCache();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.schedule_primary_color));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(SuccessEvent successEvent){
        presenter.onSuccess(successEvent.toString());
    }

    public void onEvent(FailureEvent failureEvent){
        presenter.onFailure(failureEvent.getRetrofitError());
    }

    @Override
    public void toastMessage(String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void changeWeek(String week) {
        if (!week.equals(currentWeek)){
            week += "（非当前周）";
        }
        tvWeek.setText(week);
    }

    @Override
    public void showProgress() {
        pbSchedule.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbSchedule.setVisibility(View.GONE);
    }

    @Override
    public void bindData(ClassTable classTable) {
        if (classTable.data.term.length() > 1) {
            currentTerm = classTable.data.term.substring(0, classTable.data.term.length() - 1);
//            tvScheduleTerm.setText(currentTerm + "学期课表");
        }else {
//            tvScheduleTerm.setText("现在是假期:)");
        }
        switch (currentDay) {
            case 1:
                tvSunday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 2:
                tvMonday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 3:
                tvTuesday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 4:
                tvWendesday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 5:
                tvThursday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 6:
                tvFriday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 7:
                tvSaturday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
        }
        initSchedule(classTable);
        currentWeek = classTable.data.week;
        tvWeek.setText("第" + currentWeek + "周");
    }

    private void initSchedule(ClassTable classTable){
        //绘制左侧
        for (int i = 0; i < 12; i++) {
            TextView textView = new TextView(this);
            textView.setText(i+1+"");
            textView.setWidth(griditemWidth);
            textView.setHeight(griditemWidth*2);

            GridLayout.Spec rowSpec = GridLayout.spec(i);
            GridLayout.Spec columnSpec = GridLayout.spec(0);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,columnSpec);
            params.setGravity(Gravity.CENTER_VERTICAL);

            glSchedule.addView(textView, params);

        }
        //绘制课程信息
        for (ClassTable.Data.Course course:classTable.data.data) {
            int startWeek = Integer.parseInt(course.week.start);
            int endWeek = Integer.parseInt(course.week.end);
            for (ClassTable.Data.Course.Arrange arrange: course.arrange) {
                int startTime = Integer.parseInt(arrange.start);
                int endTime = Integer.parseInt(arrange.end);
                int day = Integer.parseInt(arrange.day);
                int length = endTime - startTime + 1;

                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                CardView v = (CardView)inflater.inflate(R.layout.item_schedule,null,false);
                TextView tv = (TextView)v.findViewById(R.id.tv_schedule_class);
                tv.setText(course.coursename + "@" + arrange.room);
                tv.setWidth(griditemWidth*2);
                tv.setHeight(griditemWidth*2*length);
                tv.setTextSize(13);

                if (currentWeek >= startWeek && currentWeek <= endWeek &&
                        arrange.week.equals("单双周") ||
                        (arrange.week.equals("单周") && currentWeek % 2 == 1) ||
                        (arrange.week.equals("双周") && currentWeek % 2 == 0)){
                    if (course.coursecolor == 0){
                        Random random = new Random();
                        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
                        course.coursecolor = ranColor;
                    }
                    v.setCardBackgroundColor(course.coursecolor);
                }else {
                    v.setCardBackgroundColor(ResourceHelper.getColor(R.color.myWindowBackgroundGray));
                    tv.setTextColor(ResourceHelper.getColor(R.color.myDivider));
                }

                v.getBackground().setAlpha(90);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toClassContent(course);
                    }
                });

                GridLayout.Spec rowSpec = GridLayout.spec(startTime - 1, length);
                GridLayout.Spec columnSpec = GridLayout.spec(day+1, 1);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,columnSpec);
                params.setMargins(4,4,4,4);
                params.setGravity(Gravity.CENTER_HORIZONTAL);

                glSchedule.addView(v, params);
            }
        }
    }


    @Override
    public void startLoginActivity() {
        LoginActivity.actionStart(this, NextActivity.Schedule);
        finish();
    }

    @Override
    public void startBindActivity() {
        BindActivity.actionStart(this, NextActivity.Schedule);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.refresh:
                presenter.loadCoursesFromNet();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toClassContent(ClassTable.Data.Course course){
        // TODO: 2016/9/18 课程详情的逻辑写在这里
    }
}
