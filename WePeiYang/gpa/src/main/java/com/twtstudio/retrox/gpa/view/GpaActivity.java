package com.twtstudio.retrox.gpa.view;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

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
//        GpaItemTermBinding binding = DataBindingUtil.setContentView(this,R.layout.gpa_item_term);
//        GpaProvider.init(this)
//                .registerAction(gpaBean -> {
//                    binding.setViewModel(new TermDetailViewModel(gpaBean.data.get(2)));
//                })
//                .getData();

        mBinding = DataBindingUtil.setContentView(this,R.layout.gpa_activity_main);
        Toolbar toolbar = mBinding.toolbar;
        toolbar.setTitle("GPA");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        GpaActivityViewModel viewModel = new GpaActivityViewModel(this);
        mBinding.setViewModel(viewModel);
        viewModel.getGpaData();

    }


}
