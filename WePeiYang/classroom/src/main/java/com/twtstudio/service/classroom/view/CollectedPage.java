package com.twtstudio.service.classroom.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.databinding.ActivityRoomqueryCollectedPageBinding;
import com.twtstudio.service.classroom.model.TimeHelper;

public class CollectedPage extends RxAppCompatActivity {
    CollectedPageViewModel viewModel=new CollectedPageViewModel(this);
    ActivityRoomqueryCollectedPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomquery_collected_page);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_roomquery_collected_page);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewModel.getCollected(CommonPrefUtil.getStudentNumber(), TimeHelper.getWeekInt());
        binding.setViewModel(viewModel);
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.purple));
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
