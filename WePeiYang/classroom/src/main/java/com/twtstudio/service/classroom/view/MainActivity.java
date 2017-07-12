package com.twtstudio.service.classroom.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.databinding.ActivityMainBinding;
@Route(path = "/classroom/main")
public class MainActivity extends RxAppCompatActivity {
    ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding= DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainActivityViewModel viewModel=new MainActivityViewModel(this,46,21,3, CommonPrefUtil.getStudentNumber());
        mainBinding.setViewModel(viewModel);

    }
}
