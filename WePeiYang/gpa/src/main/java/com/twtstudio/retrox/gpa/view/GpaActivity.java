package com.twtstudio.retrox.gpa.view;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import com.twtstudio.retrox.gpa.R;
import com.twtstudio.retrox.gpa.databinding.GpaActivityMainBinding;


/**
 * Created by retrox on 2017/1/28.
 */

public class GpaActivity extends RxAppCompatActivity {

    private GpaActivityMainBinding mBinding;

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
        GpaActivityViewModel viewModel = new GpaActivityViewModel(this);
        mBinding.setViewModel(viewModel);
        viewModel.getGpaData();

    }

    @TargetApi(21)
    private void setWindowTransition(){
        getWindow().setEnterTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setSharedElementExitTransition(new ChangeBounds());

    }

}
