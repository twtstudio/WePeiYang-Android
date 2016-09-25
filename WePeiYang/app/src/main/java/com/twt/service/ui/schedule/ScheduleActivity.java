package com.twt.service.ui.schedule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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


import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.amap.api.col.c.i;

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
    //test animation
    @InjectView(R.id.schedule_parent_layout)
    LinearLayout mParentLayout;
    @InjectView(R.id.schedule_back_frame)
    FrameLayout mBackFrame;

    private SchedulePresenterImpl presenter;
    private String currentTerm;//当前学期
    private int currentWeek;// 当前周
    private int griditemWidth;
    private int currentDay;

    private int[] classColors;
    /*classTypes说明：
    * 第一维表示星期几，
    * 第二维表示课程状态：0表示无课程， 1表示有课程但是本周不上, 2表示有课程且本周上课
    * */
    boolean[][] hasClass; //
    private static final int NO_CLASS = 0;
    private static final int CLASS_NOT_THIS_WEEK = 1;
    private static final int CLASS_THIS_WEEK = 2;

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
//        currentWeek = 4;
//        tvWeek.setText("第" + currentWeek + "周");

        presenter = new SchedulePresenterImpl(this, new ScheduleInteractorImpl(), this);

        glSchedule.post(new Runnable() {
            @Override
            public void run() {
                griditemWidth = glSchedule.getWidth() / 15;
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

    public void onEvent(SuccessEvent successEvent) {
        presenter.onSuccess(successEvent.toString());
    }

    public void onEvent(FailureEvent failureEvent) {
        presenter.onFailure(failureEvent.getRetrofitError());
    }

    @Override
    public void toastMessage(String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override//只用来修改显示周数的文字
    public void changeWeek(long startUnix, int week) {
        String sWeek = "第" + TimeHelper.getWeekString(week) + "周";
        if (week != currentWeek) {
            sWeek += "(非当前周)";
        }
//        TimeHelper.getWeekDate(startUnix, week);
        tvWeek.setText(sWeek);
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
        hasClass = new boolean[7][12];
//        //修复初始情况的课程不可用bug,
        currentWeek = TimeHelper.getWeekInt(Long.parseLong(classTable.data.term_start));
        changeWeek(Long.parseLong(classTable.data.term_start),currentWeek);
        if (classTable.data.term.length() > 1) {
            currentTerm = classTable.data.term.substring(0, classTable.data.term.length() - 1);
//            tvScheduleTerm.setText(currentTerm + "学期课表");
        } else {
//            tvScheduleTerm.setText("现在是假期:)");
            tvWeek.setText("现在是假期:)");
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
    }

    private void initSchedule(ClassTable classTable) {
        //绘制左侧
        for (int i = 0; i < 12; i++) {
            TextView textView = new TextView(this);
            textView.setText(i + 1 + "");
            textView.setWidth(griditemWidth);
            textView.setHeight(griditemWidth * 2);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ResourceHelper.getColor(R.color.myTextPrimaryColorGray));
            textView.setBackgroundResource(R.color.myWindowBackgroundGray);

            GridLayout.Spec rowSpec = GridLayout.spec(i);
            GridLayout.Spec columnSpec = GridLayout.spec(0);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.setGravity(Gravity.CENTER_VERTICAL);

            glSchedule.addView(textView, params);

        }
        //绘制课程信息
        List<ClassTable.Data.Course> coursesNotThisWeek = new ArrayList<>();
        int i = 0;// 用来记录classColors用到第几个了。
        if (classColors == null) {
            classColors = new int[]{R.color.schedule_green,
                    R.color.schedule_orange,
                    R.color.schedule_blue,
                    R.color.schedule_green2,
                    R.color.schedule_pink,
                    R.color.schedule_blue2,
                    R.color.schedule_green3,
                    R.color.schedule_purple,
                    R.color.schedule_red,
                    R.color.schedule_green4,
                    R.color.schedule_purple2};
        }
        //绘制当前周的课程
        for (ClassTable.Data.Course course : classTable.data.data) {
            int startWeek = Integer.parseInt(course.week.start);
            int endWeek = Integer.parseInt(course.week.end);
            for (ClassTable.Data.Course.Arrange arrange : course.arrange) {
                int startTime = Integer.parseInt(arrange.start);
                int endTime = Integer.parseInt(arrange.end);
                int day = Integer.parseInt(arrange.day);
                int length = endTime - startTime + 1;
                if (course.coursecolor == 0) {
                    if (i == 11) {
                        i = 0;
                    }
                    course.coursecolor = ResourceHelper.getColor(classColors[i]);
                    i++;
                }
                if (currentWeek >= startWeek && currentWeek <= endWeek &&
                        arrange.week.equals("单双周") ||
                        (arrange.week.equals("单周") && currentWeek % 2 == 1) ||
                        (arrange.week.equals("双周") && currentWeek % 2 == 0)) {
                    if (!hasClassThisWeek(day, startTime, endTime)) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = inflater.inflate(R.layout.item_schedule, null, false);
                        CardView cv = (CardView) v.findViewById(R.id.cv_s);
                        TextView tv = (TextView) v.findViewById(R.id.tv_schedule_class);
                        tv.setText(course.coursename + "@" + arrange.room);
                        tv.setWidth(griditemWidth * 2 - 8);
                        tv.setHeight(griditemWidth * 2 * length - 8);
                        tv.setTextSize(13);
                        cv.setCardBackgroundColor(course.coursecolor);
                        cv.getCardBackgroundColor().withAlpha(90);

                        cv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                toClassContent(classTable.data.data,day,startTime,endTime,cv);
//                                moveToContent(course, cv);
                            }
                        });

                        GridLayout.Spec rowSpec = GridLayout.spec(startTime - 1, length);
                        GridLayout.Spec columnSpec = GridLayout.spec(day + 1, 1);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                        params.setGravity(Gravity.CENTER_HORIZONTAL);
                        params.setMargins(4, 4, 4, 4);
                        glSchedule.addView(v, params);
                        for (int t = startTime - 1; t < endTime; t++) {
                            hasClass[day][t] = true;
                        }
                    }
                } else {
                    // TODO: 2016/9/25 把course放入一个飞当前周的list
                    coursesNotThisWeek.add(course);
                }
            }
        }
        //绘制非本周课程信息
        for (ClassTable.Data.Course course : coursesNotThisWeek) {
            int startWeek = Integer.parseInt(course.week.start);
            int endWeek = Integer.parseInt(course.week.end);
            for (ClassTable.Data.Course.Arrange arrange : course.arrange) {
                int startTime = Integer.parseInt(arrange.start);
                int endTime = Integer.parseInt(arrange.end);
                int day = Integer.parseInt(arrange.day);
                int length = endTime - startTime + 1;
                if (!hasClassThisWeek(day, startTime, endTime)) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = inflater.inflate(R.layout.item_schedule, null, false);
                    CardView cv = (CardView) v.findViewById(R.id.cv_s);
                    TextView tv = (TextView) v.findViewById(R.id.tv_schedule_class);
                    tv.setText(course.coursename + "@" + arrange.room);
                    tv.setWidth(griditemWidth * 2 - 8);
                    tv.setHeight(griditemWidth * 2 * length - 8);
                    tv.setTextSize(13);

                    cv.setCardBackgroundColor(ResourceHelper.getColor(R.color.myWindowBackgroundGray));
                    tv.setTextColor(ResourceHelper.getColor(R.color.schedule_gray));
                    cv.getCardBackgroundColor().withAlpha(90);

                    cv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toClassContent(classTable.data.data,day,startTime,endTime,cv);
//                            moveToContent(course, cv);
                        }
                    });

                    GridLayout.Spec rowSpec = GridLayout.spec(startTime - 1, length);
                    GridLayout.Spec columnSpec = GridLayout.spec(day + 1, 1);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                    params.setGravity(Gravity.CENTER_HORIZONTAL);
                    params.setMargins(4, 4, 4, 4);
                    glSchedule.addView(v, params);
                    for (int t = startTime - 1; t < endTime; t++) {
                        hasClass[day][t] = true;
                    }
                }
            }
        }
    }


    private boolean hasClassThisWeek(int day, int startTime, int endTime) {
        for (int t = startTime - 1; t < endTime; t++) {
            if (hasClass[day][t]) {
                return true;
            }
        }
        return false;
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
    //这段会判断是否有多个课程出现重复的情况。第一个参数传入所有的课程，第二个传入点击课程的星期，第三、四个传入课程的起始时间，第五个传入控件
    private void toClassContent(List<ClassTable.Data.Course> courses,int day, int startTime, int endTime, View v){
        List<ClassTable.Data.Course> coursesInThisTime = new ArrayList<>();
        for (ClassTable.Data.Course course: courses) {
            for (ClassTable.Data.Course.Arrange arrange: course.arrange){
                int sT = Integer.parseInt(arrange.start);
                int eT = Integer.parseInt(arrange.end);
                int d = Integer.parseInt(arrange.day);
                if (d == day && sT >= startTime && eT <= endTime){
                    coursesInThisTime.add(course);
                }
            }
        }
        if (coursesInThisTime.size() == 1){
            moveToContent(coursesInThisTime.get(0),v);
        }else {
            // TODO: 2016/9/25 这里表示有多个课程的情况，直接使用courseInThisTime即可
        }
    }
    // TODO: 16-9-20 圆形扩散
    private void toClassContent(ClassTable.Data.Course course, View view) {
        // TODO: 2016/9/18 课程详情的逻辑写在这里
        int color = course.coursecolor;
        int px = mParentLayout.getWidth();
        int py = mParentLayout.getHeight();
        float radius = (float) Math.hypot(px, py);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        Animator animator = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(mBackFrame, x + view.getWidth() / 2, y - view.getHeight() / 4, 10, radius);

            animator.addListener(new AnimatorListenerAdapter() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mBackFrame.setBackgroundColor(Color.TRANSPARENT);

                    Intent intent = new Intent(ScheduleActivity.this, ScheduleDetailsActivity.class);
                    intent.putExtra("color", color);
                    //Pair<View,String> pair = new Pair<View, String>(view,getString(R.string.schedule_transition));
                    //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ScheduleActivity.this,pair);

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ScheduleActivity.this, view, getString(R.string.schedule_transition));
                    startActivity(intent, options.toBundle());
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mBackFrame.setBackgroundColor(color);
                }
            });
        }
        animator.setDuration(1000);
        animator.start();
    }

    //另一种扩散
    private void moveToContent(ClassTable.Data.Course course, View view) {
        String transName = getString(R.string.schedule_transition);
        Intent intent = new Intent(ScheduleActivity.this, ScheduleDetailsActivity.class);
        intent.putExtra("color", course.coursecolor);
//        intent.putExtra("course", course);

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("course",course);

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(ScheduleActivity.this, view, getString(R.string.schedule_transition));
        startActivity(intent, activityOptions.toBundle());
    }

}
