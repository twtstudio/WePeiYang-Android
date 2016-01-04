package com.rex.wepeiyang.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.ClassTable;
import com.rex.wepeiyang.interactor.ScheduleInteractorImpl;
import com.rex.wepeiyang.support.StatusBarHelper;
import com.rex.wepeiyang.ui.BaseActivity;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    @InjectView(R.id.rv_schedule)
    RecyclerView rvSchedule;
    @InjectView(R.id.pb_schedule)
    ProgressBar pbSchedule;
    @InjectView(R.id.tv_schedule_term)
    TextView tvScheduleTerm;
    private SchedulePresenter presenter;
    private String currentTerm;//当前学期
    private ScheduleAdapter adapter;
    private int currentDay;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        adapter = new ScheduleAdapter(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvSchedule.setAdapter(adapter);
        rvSchedule.setLayoutManager(manager);
        presenter = new SchedulePresenterImpl(this, new ScheduleInteractorImpl());
        presenter.loadCourses();
        //StatusBarHelper.setStatusBar(this, getResources().getColor(R.color.schedule_primary_color));
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
        currentTerm = classTable.data.term.substring(0, classTable.data.term.length() - 1);
        tvScheduleTerm.setText(currentTerm + "学期课表");
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
        adapter.bindData(classTable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
