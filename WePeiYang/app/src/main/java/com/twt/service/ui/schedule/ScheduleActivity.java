package com.twt.service.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.ClassTable;
import com.twt.service.interactor.ScheduleInteractorImpl;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.bind.BindActivity;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.login.LoginActivity;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
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
    @InjectView(R.id.rv_schedule)
    RecyclerView rvSchedule;
    @InjectView(R.id.pb_schedule)
    ProgressBar pbSchedule;
    @InjectView(R.id.tv_schedule_term)
    TextView tvScheduleTerm;
    private SchedulePresenterImpl presenter;
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
        EventBus.getDefault().register(this);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        adapter = new ScheduleAdapter(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvSchedule.setAdapter(adapter);
        rvSchedule.setLayoutManager(manager);
        presenter = new SchedulePresenterImpl(this, new ScheduleInteractorImpl(), this);
        presenter.loadCoursesFromCache();
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
            tvScheduleTerm.setText(currentTerm + "学期课表");
        }else {
            tvScheduleTerm.setText("现在是假期:)");
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
        adapter.bindData(classTable);
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
}
