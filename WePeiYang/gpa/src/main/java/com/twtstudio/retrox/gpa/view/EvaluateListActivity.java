package com.twtstudio.retrox.gpa.view;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.MenuItem;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.R;
import com.twtstudio.retrox.gpa.databinding.GpaActivityEvaluateListBinding;

import java.util.ArrayList;

/**
 * Created by tjliqy on 2017/6/2.
 */

public class EvaluateListActivity extends RxAppCompatActivity {

    private GpaActivityEvaluateListBinding mBinding;
    private EvaluateListViewModel viewModel;
    ArrayList<GpaBean.Term.Course> unEvaluatedCourses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            setWindowTransition();
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.gpa_activity_evaluate_list);
        Toolbar toolbar = mBinding.toolbar;
        toolbar.setTitle("GPA");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewModel = new EvaluateListViewModel(this);
        mBinding.setViewModel(viewModel);

        unEvaluatedCourses = (ArrayList<GpaBean.Term.Course>) getIntent().getSerializableExtra("key");
        viewModel.setCourses(unEvaluatedCourses);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.gpa_coordinator);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(21)
    private void setWindowTransition(){
        getWindow().setEnterTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setSharedElementExitTransition(new ChangeBounds());

    }
}
