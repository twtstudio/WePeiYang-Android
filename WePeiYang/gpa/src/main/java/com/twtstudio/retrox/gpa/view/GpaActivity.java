package com.twtstudio.retrox.gpa.view;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import com.twtstudio.retrox.gpa.R;
import com.twtstudio.retrox.gpa.databinding.GpaActivityMainBinding;


/**
 * Created by retrox on 2017/1/28.
 */

@Route(path = "/gpa/main")
public class GpaActivity extends RxAppCompatActivity {

    private GpaActivityMainBinding mBinding;
    private GpaActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            setWindowTransition();
        }
        mBinding = DataBindingUtil.setContentView(this,R.layout.gpa_activity_main);
        Toolbar toolbar = mBinding.toolbar;
        toolbar.setTitle("GPA");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewModel = new GpaActivityViewModel(this);
        mBinding.setViewModel(viewModel);
        viewModel.getGpaData();

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.gpa_coordinator);
        Snackbar snackbar = Snackbar.make(coordinatorLayout,"点击图中的圆圈切换成绩列表所属学期",Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }else if (item.getItemId() == R.id.refresh){
            viewModel.getGpaData(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gpa, menu);
        return true;
    }

    @TargetApi(21)
    private void setWindowTransition(){
        getWindow().setEnterTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setSharedElementExitTransition(new ChangeBounds());

    }

}
