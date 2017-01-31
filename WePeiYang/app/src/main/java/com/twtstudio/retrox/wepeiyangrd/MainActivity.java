package com.twtstudio.retrox.wepeiyangrd;

import android.os.Bundle;
import android.widget.Button;

import com.twt.wepeiyang.commons.auth.login.LoginViewModel;
import com.twtstudio.retrox.wepeiyangrd.base.BaseActivity;
import com.twtstudio.retrox.wepeiyangrd.base.BaseFragment;
import com.twtstudio.retrox.wepeiyangrd.home.common.oneItem.OneInfoViewModel;



public class MainActivity extends BaseActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    private void test1(){
        BaseFragment fragment = new BaseFragment();
        OneInfoViewModel viewModel = new OneInfoViewModel(fragment);
    }

    private void test2(){
//        GpaViewModel viewModel = new GpaViewModel(this);
        //viewModel.getData();
    }

}
