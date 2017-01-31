package com.twt.wepeiyang.commons.auth.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.R;
import com.twt.wepeiyang.commons.databinding.ActivityLoginBinding;


/**
 * Created by retrox on 2016/11/27.
 */

public class LoginActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(new LoginViewModel(this));

//        LoginViewModel viewModel = new LoginViewModel(this);
//        viewModel.twtuName.set("miss976885345");
//        viewModel.twtpasswd.set("JCYwin551100");
//        viewModel.onLoginClickCommand.execute();
    }
}
