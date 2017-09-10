package com.twt.service;

import android.os.Bundle;
import android.widget.Button;

import com.twtstudio.retrox.auth.login.LoginViewModel;
import com.twt.service.base.BaseActivity;
import com.twt.service.base.BaseFragment;
import com.twt.service.home.common.oneItem.OneInfoViewModel;


public class MainActivity extends BaseActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_query_main);


        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(v -> test2());

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(v -> test());
    }

    private void test() {

        LoginViewModel viewModel = new LoginViewModel(this);
        viewModel.twtuName.set("test");
        viewModel.twtpasswd.set("test");
        viewModel.onLoginClickCommand.execute();
    }

    private void test1() {
        BaseFragment fragment = new BaseFragment();
        OneInfoViewModel viewModel = new OneInfoViewModel(fragment);
    }

    private void test2() {
//        Intent intent = new Intent(this, BikeActivity.class);
//        startActivity(intent);
    }

}
