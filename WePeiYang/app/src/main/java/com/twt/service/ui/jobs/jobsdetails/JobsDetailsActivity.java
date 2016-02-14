package com.twt.service.ui.jobs.jobsdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.Jobs;
import com.twt.service.interactor.JobsInteractorImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class JobsDetailsActivity extends AppCompatActivity implements JobsDetailsView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tv_jobs_details_title)
    TextView tvJobsDetailsTitle;
    @InjectView(R.id.tv_jobs_details_date)
    TextView tvJobsDetailsDate;
    @InjectView(R.id.tv_jobs_details_corporation)
    TextView tvJobsDetailsCorporation;
    @InjectView(R.id.tv_jobs_details_view_count)
    TextView tvJobsDetailsViewCount;
    @InjectView(R.id.wv_jobs_detials)
    WebView wvJobsDetials;
    @InjectView(R.id.pb_jobs_details)
    ProgressBar pbJobsDetails;
    private JobsDetailsPresenterImpl presenter;
    private int id;

    public static void actionStart(Context context, int id) {
        Intent intent = new Intent(context, JobsDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_details);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new JobsDetailsPresenterImpl(this, new JobsInteractorImpl());
        wvJobsDetials.getSettings().setJavaScriptEnabled(true);
        wvJobsDetials.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return (motionEvent.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        wvJobsDetials.setScrollbarFadingEnabled(true);
        wvJobsDetials.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        id = getIntent().getIntExtra("id", 0);
        presenter.getJobsDetails(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.news_primary_color));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    public void onEvent(SuccessEvent successEvent) {
        presenter.onSuccess(successEvent.getJobs());
    }

    public void onEvent(FailureEvent failureEvent) {
        presenter.onFailure(failureEvent.getError());
    }

    @Override
    public void showProgress() {
        pbJobsDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbJobsDetails.setVisibility(View.GONE);
    }

    @Override
    public void bindData(Jobs jobs) {
        tvJobsDetailsTitle.setText(jobs.data.title);
        tvJobsDetailsCorporation.setText(jobs.data.corporation);
        tvJobsDetailsDate.setText(jobs.data.date);
        tvJobsDetailsViewCount.setText(jobs.data.click + "");
        wvJobsDetials.loadDataWithBaseURL("", jobs.data.content, "text/html", "UTF-8", "");
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
