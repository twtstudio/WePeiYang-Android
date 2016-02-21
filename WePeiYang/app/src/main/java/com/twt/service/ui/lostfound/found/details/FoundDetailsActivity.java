package com.twt.service.ui.lostfound.found.details;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twt.service.R;
import com.twt.service.bean.FoundDetails;
import com.twt.service.interactor.FoundInteractorImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class FoundDetailsActivity extends AppCompatActivity implements FoundDetailsView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.iv_found_pic)
    ImageView ivFoundPic;
    @InjectView(R.id.tv_found_title)
    TextView tvFoundTitle;
    @InjectView(R.id.tv_found_detail_time)
    TextView tvFoundDetailTime;
    @InjectView(R.id.tv_found_detail_position)
    TextView tvFoundDetailPosition;
    @InjectView(R.id.tv_found_detail_name)
    TextView tvFoundDetailName;
    @InjectView(R.id.tv_found_detail_phone)
    TextView tvFoundDetailPhone;
    @InjectView(R.id.tv_found_detail_description)
    TextView tvFoundDetailDescription;
    @InjectView(R.id.pb_lost_details)
    ProgressBar pbLostDetails;
    private int id;
    private FoundDetailsPresenterImpl presenter;

    public static void actionStart(Context context, int id) {
        Intent intent = new Intent(context, FoundDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_details);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getIntExtra("id", 0);
        presenter = new FoundDetailsPresenterImpl(this, new FoundInteractorImpl());
        presenter.getFoundDetails(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.lost_found_primary_color));
        }

    }

    public void onEvent(SuccessEvent successEvent) {
        presenter.onSuccess(successEvent.getDetails());
    }

    public void onEvent(FailureEvent failureEvent) {
        presenter.onFailure(failureEvent.getError());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void showProgress() {
        pbLostDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLostDetails.setVisibility(View.GONE);
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindData(FoundDetails details) {
        if (!details.data.found_pic.isEmpty()) {
            Picasso.with(this).load(details.data.found_pic).into(ivFoundPic);
        } else {
            ivFoundPic.setImageResource(R.mipmap.icon_others);
        }
        tvFoundTitle.setText(details.data.title);
        tvFoundDetailTime.setText(details.data.time);
        tvFoundDetailPosition.setText(details.data.place);
        tvFoundDetailName.setText(details.data.name);
        tvFoundDetailPhone.setText(details.data.phone);
        tvFoundDetailDescription.setText(details.data.content);

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
