package com.twtstudio.retrox.schedule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.tencent.bugly.crashreport.CrashReport;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@Route(path = "/schedule/main")
public class ScheduleActivity extends RxAppCompatActivity implements ScheduleView {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.tv_monday)
    TextView tvMonday;
    @BindView(R2.id.tv_tuesday)
    TextView tvTuesday;
    @BindView(R2.id.tv_wednesday)
    TextView tvWednesday;
    @BindView(R2.id.tv_thursday)
    TextView tvThursday;
    @BindView(R2.id.tv_friday)
    TextView tvFriday;
    @BindView(R2.id.tv_saturday)
    TextView tvSaturday;
    @BindView(R2.id.tv_sunday)
    TextView tvSunday;
    @BindView(R2.id.pb_schedule)
    ProgressBar pbSchedule;
    @BindView(R2.id.tv_week)
    TextView tvWeek;
    //test animation
    @BindView(R2.id.schedule_parent_layout)
    LinearLayout mParentLayout;
    @BindView(R2.id.schedule_back_frame)
    FrameLayout mBackFrame;
    @BindView(R2.id.ll_monday)
    LinearLayout mLlMonday;
    @BindView(R2.id.ll_tuesday)
    LinearLayout mLlTuesday;
    @BindView(R2.id.ll_wednesday)
    LinearLayout mLlWednesday;
    @BindView(R2.id.ll_thursday)
    LinearLayout mLlThursday;
    @BindView(R2.id.ll_friday)
    LinearLayout mLlFriday;
    @BindView(R2.id.ll_saturday)
    LinearLayout mLlSaturday;
    @BindView(R2.id.ll_sunday)
    LinearLayout mLlSunday;
    @BindView(R2.id.ll_nums)
    LinearLayout mLlNums;
    @BindView(R2.id.rl_monday)
    RelativeLayout mRlMonday;
    @BindView(R2.id.rl_tuesday)
    RelativeLayout mRlTuesday;
    @BindView(R2.id.rl_wednesday)
    RelativeLayout mRlWednesday;
    @BindView(R2.id.rl_thursday)
    RelativeLayout mRlThursday;
    @BindView(R2.id.rl_friday)
    RelativeLayout mRlFriday;
    @BindView(R2.id.rl_saturday)
    RelativeLayout mRlSaturday;
    @BindView(R2.id.rl_sunday)
    RelativeLayout mRlSunday;
    @BindView(R2.id.refresh)
    SwipeRefreshLayout refresh;


    private String currentTerm;//当前学期
    private int currentWeek;// 当前周
    private int griditemWidth;
    private int currentDay;
    List<WeekItem> items;
    private int[] classColors;
    private RecyclerPopupWindow recyclerPopupWindow;
    /*classTypes说明：
    * 第一维表示星期几，
    * 第二维表示课程状态：0表示无课程， 1表示有课程但是本周不上, 2表示有课程且本周上课
    * */
    boolean[][] hasClass; //
    private static final int NO_CLASS = 0;
    private static final int CLASS_NOT_THIS_WEEK = 1;
    private static final int CLASS_THIS_WEEK = 2;
    private ClassTable mClassTable;

    private Unbinder mUnbinder;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        //判断默认显示的Activity
        if (CommonPrefUtil.getIsNewSchedule()) {
            Intent intent = new Intent(this, ScheduleNewActivity.class);
            startActivity(intent);
            finish();
        }
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_WEEK);
//        currentWeek = 4;
//        tvWeek.setText("第" + currentWeek + "周");

        //更新窗口小部件
        Intent intent = new Intent("com.twt.appwidget.refresh");
        this.sendBroadcast(intent);

        mLlNums.post(() -> {
            griditemWidth = mLlNums.getWidth();

            //获取数据
            getScheduleDataAuto(false);

            //绘制左侧
            for (int i = 0; i < 12; i++) {
                TextView textView = new TextView(this);
                textView.setText(i + 1 + "");
                textView.setWidth(griditemWidth);
                textView.setHeight(griditemWidth * 2);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(ResourceHelper.getColor(R.color.myTextPrimaryColorGray));
                textView.setBackgroundResource(R.color.myWindowBackgroundGray);
//            GridLayout.Spec rowSpec = GridLayout.spec(i);
//            GridLayout.Spec columnSpec = GridLayout.spec(0);
//            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
//            params.setGravity(Gravity.CENTER_VERTICAL);

                mLlNums.addView(textView);

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.schedule_primary_color));
        }


        //初始化周数下拉列表的List
        items = new ArrayList<>();
        for (int i = 0; i < 25; ++i) {
            items.add(i, new WeekItem(i + 1, false));

        }

        refresh.setOnRefreshListener(() -> {
            getScheduleDataAuto(true);
        });


    }

    private void getScheduleDataAuto(boolean refresh) {
//        showProgress();
        ClassTableProvider.init(this)
                .registerAction(classTable -> {
//                    hideProgress();
                    this.refresh.setRefreshing(false);
                    bindData(classTable);

                })
                .getData(refresh);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }


    @Override
    public void toastMessage(String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    //只用来修改显示周数的文字
    public void changeWeek(int week) {
        StringBuilder sWeek = new StringBuilder("第" + TimeHelper.getWeekString(week) + "周");
        if (week != currentWeek) {
            sWeek.append("(非当前周)");
        }
        sWeek.append("▼");
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
        mClassTable = classTable;

//        //修复初始情况的课程不可用bug,
        currentWeek = TimeHelper.getWeekInt(Long.parseLong(classTable.data.term_start), Calendar.getInstance());
        if (currentWeek >= 0){
            changeWeek(currentWeek);
        }
        if (classTable.data.term.length() > 1 && currentWeek >= 0) {
            currentTerm = classTable.data.term.substring(0, classTable.data.term.length() - 1);
//            tvScheduleTerm.setText(currentTerm + "学期课表");
        } else {
//            tvScheduleTerm.setText("现在是假期:)");
            tvWeek.setText("现在是假期:)");
        }
        switch (currentDay) {
            case 1:
                mLlSunday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 2:
                mLlMonday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 3:
                mLlTuesday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 4:
                mLlWednesday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 5:
                mLlThursday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 6:
                mLlFriday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
            case 7:
                mLlSaturday.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color));
                break;
        }

        tvWeek.setOnClickListener(view -> {
            if (recyclerPopupWindow == null) {
                recyclerPopupWindow = new RecyclerPopupWindow(items, currentWeek);
                recyclerPopupWindow.showPopupWindow(ScheduleActivity.this, toolbar, toolbar.getWidth(), 500);
                recyclerPopupWindow.setCallBack((value, week_num) -> {
                    if (!"-1".equals(value)) {
                        changeWeek(week_num);
                        if (mClassTable != null) {
                            mRlMonday.removeAllViews();
                            mRlTuesday.removeAllViews();
                            mRlWednesday.removeAllViews();
                            mRlThursday.removeAllViews();
                            mRlFriday.removeAllViews();
                            mRlSaturday.removeAllViews();
                            mRlSunday.removeAllViews();

                            initSchedule(mClassTable, week_num, false);
                        }
                    }
                    recyclerPopupWindow = null;
                });
            }
        });
        initSchedule(classTable, currentWeek, true);
    }

    private void initSchedule(ClassTable classTable, int week, boolean isUpdate) {
        hasClass = new boolean[7][12];
        if (isUpdate) {
            mRlMonday.removeAllViews();
            mRlTuesday.removeAllViews();
            mRlWednesday.removeAllViews();
            mRlThursday.removeAllViews();
            mRlFriday.removeAllViews();
            mRlSaturday.removeAllViews();
            mRlSunday.removeAllViews();
        }

        //绘制课程信息
        Set<ClassTable.Data.Course> coursesNotThisWeek = new HashSet<>(); //非当前周的课程
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
                if ((week >= startWeek && week <= endWeek) &&
                        (arrange.week.equals("单双周") ||
                                (arrange.week.equals("单周") && week % 2 == 1) ||
                                (arrange.week.equals("双周") && week % 2 == 0))) {
                    //缓存课程当前周状态
                    course.isAvaiableCurrentWeek = true;

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
                        cv.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
                        cv.setOnClickListener(view -> {
                            toClassContent(classTable.data.data, day, startTime, endTime, cv);
//                                moveToContent(course, cv);
                        });
                        v.setY((startTime - 1) * griditemWidth * 2);
//                        GridLayout.Spec rowSpec = GridLayout.spec(startTime - 1, length);
//                        GridLayout.Spec columnSpec = GridLayout.spec(day + 1, 1);
//                        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);

//                        params.setGravity(Gravity.CENTER_HORIZONTAL);
//                        params.setMargins(4, 4, 4, 4);
                        switch (day) {
                            case 1:
                                mRlMonday.addView(v);
                                break;
                            case 2:
                                mRlTuesday.addView(v);
                                break;
                            case 3:
                                mRlWednesday.addView(v);
                                break;
                            case 4:
                                mRlThursday.addView(v);
                                break;
                            case 5:
                                mRlFriday.addView(v);
                                break;
                            case 6:
                                mRlSaturday.addView(v);
                                break;
                            case 7:
                                mRlSunday.addView(v);
                                break;
                        }
//                        glSchedule.addView(v, params);
                        for (int t = startTime - 1; t < endTime; t++) {
                            hasClass[day][t] = true;
                        }
                    } else {
                        // 多节课程逻辑
                        addMultiClassLabel(day, startTime, endTime);
                    }
                } else {
                    course.isAvaiableCurrentWeek = false;
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
                    //跳转详情页面需要灰色
//                    course.coursecolor = ResourceHelper.getColor(R.color.schedule_gray);
                    cv.setCardBackgroundColor(ResourceHelper.getColor(R.color.myWindowBackgroundGray));
                    tv.setTextColor(ResourceHelper.getColor(R.color.schedule_gray));
                    cv.getCardBackgroundColor().withAlpha(90);

                    cv.setOnClickListener(view -> {
                        toClassContent(classTable.data.data, day, startTime, endTime, cv);
//                            moveToContent(course, cv);
                    });
                    cv.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
                    v.setY((startTime - 1) * griditemWidth * 2);

//                    GridLayout.Spec rowSpec = GridLayout.spec(startTime - 1, length);
//                    GridLayout.Spec columnSpec = GridLayout.spec(day + 1, 1);
//                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
//                    params.setGravity(Gravity.CENTER_HORIZONTAL);
//                    params.setMargins(4, 4, 4, 4);

                    switch (day) {
                        case 1:
                            mRlMonday.addView(v);
                            break;
                        case 2:
                            mRlTuesday.addView(v);
                            break;
                        case 3:
                            mRlWednesday.addView(v);
                            break;
                        case 4:
                            mRlThursday.addView(v);
                            break;
                        case 5:
                            mRlFriday.addView(v);
                            break;
                        case 6:
                            mRlSaturday.addView(v);
                            break;
                        case 7:
                            mRlSunday.addView(v);
                            break;
                    }
//                    glSchedule.addView(v, params);
                    for (int t = startTime - 1; t < endTime; t++) {
                        hasClass[day][t] = true;
                    }
                } else {
                    //  多节课程逻辑
                    addMultiClassLabel(day, startTime, endTime);
                }
            }
        }
    }

    private void addMultiClassLabel(int day, int startTime, int endTime) {
        int length = endTime - startTime + 1;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_schedule_label, null, false);

        v.setY((startTime - 1) * griditemWidth * 2 + griditemWidth * 2 * length - 8 - dip2px(30));
        switch (day) {
            case 1:
                mRlMonday.addView(v);
                break;
            case 2:
                mRlTuesday.addView(v);
                break;
            case 3:
                mRlWednesday.addView(v);
                break;
            case 4:
                mRlThursday.addView(v);
                break;
            case 5:
                mRlFriday.addView(v);
                break;
            case 6:
                mRlSaturday.addView(v);
                break;
            case 7:
                mRlSunday.addView(v);
                break;
        }

    }

    public int dip2px(float dpValue) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private boolean hasClassThisWeek(int day, int startTime, int endTime) {
        try {
            for (int t = startTime - 1; t < endTime; t++) {
                if (hasClass[day][t]) {
                    return true;
                }
            }
        } catch (Exception e) {
            CrashReport.postCatchedException(e);
        }

        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.schedule_switch) {
            //switch
            Intent intent = new Intent(this, ScheduleNewActivity.class);
            startActivity(intent);
            CommonPrefUtil.setIsNewSchedule(true);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    //这段会判断是否有多个课程出现重复的情况。第一个参数传入所有的课程，第二个传入点击课程的星期，第三、四个传入课程的起始时间，第五个传入控件
    private void toClassContent(List<ClassTable.Data.Course> courses, int day, int startTime, int endTime, View v) {
        List<ClassTable.Data.Course> coursesInThisTime = new ArrayList<>();
        for (ClassTable.Data.Course course : courses) {
            for (ClassTable.Data.Course.Arrange arrange : course.arrange) {
                int sT = Integer.parseInt(arrange.start);
                int eT = Integer.parseInt(arrange.end);
                int d = Integer.parseInt(arrange.day);
                if (d == day && sT >= startTime && eT <= endTime) {
                    coursesInThisTime.add(course);
                }
            }
        }
        if (coursesInThisTime.size() == 1) {
            moveToContent(coursesInThisTime.get(0), v);
        } else {
            moveToMultiContent(coursesInThisTime, v);
        }
    }

    //圆形扩散
    private void toClassContent(ClassTable.Data.Course course, View view) {
        int color = course.coursecolor;
        int px = mParentLayout.getWidth();
        int py = mParentLayout.getHeight();
        float radius = (float) Math.hypot(px, py);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        Animator animator = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        intent.putExtra("course", course);

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("course",course);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(ScheduleActivity.this, view, getString(R.string.schedule_transition));
            startActivity(intent, activityOptions.toBundle());
        } else {
            startActivity(intent);
        }

    }

    private void moveToMultiContent(List<ClassTable.Data.Course> coursesInThisTime, View view) {
        Intent intent = new Intent(ScheduleActivity.this, MultiCourseActivity.class);
        double card_x = view.getWidth();
        double card_y = view.getHeight();
        //计算出如何缩放多课程选择窗口
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        int window_x = point.x;
        int window_y = point.y;
        card_y *= 2.0;
        card_x *= 5.0;
        double percent_x = card_x / window_x;
        double percent_y = card_y / window_y;
        intent.putExtra("percent_x", percent_x);
        intent.putExtra("percent_y", percent_y);
        //装入前两个课程
        intent.putExtra("course1", coursesInThisTime.get(0));
        intent.putExtra("course2", coursesInThisTime.get(1));

        //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ScheduleActivity.this,view,getString(R.string.schedule_transition));

        startActivity(intent);
    }

}
