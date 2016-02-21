package com.twt.service.ui.lostfound.lost.details;

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

import com.twt.service.R;
import com.twt.service.bean.LostDetails;
import com.twt.service.interactor.LostInteractorImpl;
import com.twt.service.ui.common.LostType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class LostDetailsActivity extends AppCompatActivity implements LostDetailsView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.iv_lost_type)
    ImageView ivLostType;
    @InjectView(R.id.tv_lost_title)
    TextView tvLosttitle;
    @InjectView(R.id.tv_lost_detail_time)
    TextView tvLostDetailTime;
    @InjectView(R.id.tv_lost_detail_position)
    TextView tvLostDetailPosition;
    @InjectView(R.id.tv_lost_detail_name)
    TextView tvLostDetailName;
    @InjectView(R.id.tv_lost_detail_phone)
    TextView tvLostDetailPhone;
    @InjectView(R.id.tv_lost_detail_description)
    TextView tvLostDetailDescription;
    @InjectView(R.id.pb_lost_details)
    ProgressBar pbLostDetails;
    private LostDetailsPresenterImpl presenter;
    private int id;


    public static void actionStart(Context context, int id) {
        Intent intent = new Intent(context, LostDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_details);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getIntExtra("id", id);
        presenter = new LostDetailsPresenterImpl(this, new LostInteractorImpl());
        presenter.getLostDetails(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.lost_found_primary_color));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(SuccessEvent successEvent) {
        presenter.onSuccess(successEvent.getLostDetails());
    }

    public void onEvent(FailureEvent failureEvent) {
        presenter.onFailure(failureEvent.getError());
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
    public void bindData(LostDetails details) {
        switch (details.data.lost_type) {
            case LostType.OTHERS:
                ivLostType.setImageResource(R.mipmap.icon_others);
                break;
            case LostType.BANK_CARD:
                ivLostType.setImageResource(R.mipmap.icon_bank_card);
                break;
            case LostType.ID_CARD:
                ivLostType.setImageResource(R.mipmap.icon_id_card);
                break;
            case LostType.KEY:
                ivLostType.setImageResource(R.mipmap.icon_key);
                break;
            case LostType.BACKPACK:
                ivLostType.setImageResource(R.mipmap.icon_backpack);
                break;
            case LostType.COMPUTER_PAG:
                ivLostType.setImageResource(R.mipmap.icon_computer_pag);
                break;
            case LostType.WATCH:
                ivLostType.setImageResource(R.mipmap.icon_watch);
                break;
            case LostType.UDISK:
                ivLostType.setImageResource(R.mipmap.icon_udisk);
                break;
            case LostType.CUP:
                ivLostType.setImageResource(R.mipmap.icon_cup);
                break;
            case LostType.BOOK:
                ivLostType.setImageResource(R.mipmap.icon_books);
                break;
            case LostType.MOBILE_PHONE:
                ivLostType.setImageResource(R.mipmap.icon_mobile_phone);
                break;
            case LostType.WALLET:
                ivLostType.setImageResource(R.mipmap.icon_wallet);
                break;
        }
        tvLosttitle.setText(details.data.title);
        tvLostDetailTime.setText(details.data.time);
        tvLostDetailPosition.setText(details.data.place);
        tvLostDetailName.setText(details.data.name);
        tvLostDetailPhone.setText(details.data.phone);
        tvLostDetailDescription.setText(details.data.content);
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
