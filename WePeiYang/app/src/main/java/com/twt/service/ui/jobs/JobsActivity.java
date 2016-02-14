package com.twt.service.ui.jobs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.JobsList;
import com.twt.service.interactor.JobsInteractorImpl;
import com.twt.service.ui.common.OnRcvScrollListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class JobsActivity extends AppCompatActivity implements JobsView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.rv_jobs)
    RecyclerView rvJobs;
    @InjectView(R.id.srl_jobs)
    SwipeRefreshLayout srlJobs;
    private JobsPresenterImpl presenter;
    private JobsAdapter adapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, JobsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        presenter = new JobsPresenterImpl(this, new JobsInteractorImpl());
        adapter = new JobsAdapter(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        srlJobs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        srlJobs.setColorSchemeColors(getResources().getColor(R.color.news_primary_color));
        rvJobs.setAdapter(adapter);
        rvJobs.setLayoutManager(new LinearLayoutManager(this));
        rvJobs.setOnScrollListener(new OnRcvScrollListener(){
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.loadMore();
            }
        });
        presenter.refresh();
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
        presenter.onSuccess(successEvent.getJobsList());
    }

    public void onEvent(FailureEvent failureEvent) {
        presenter.onFailure(failureEvent.getError());
    }


    @Override
    public void hideRefreshing() {
        srlJobs.setRefreshing(false);
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh(JobsList jobsList) {
        adapter.refresh(jobsList.data);
    }

    @Override
    public void loadMore(JobsList jobsList) {
        adapter.add(jobsList.data);
    }
}
