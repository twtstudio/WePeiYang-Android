package com.twt.service;

import android.os.Bundle;
import android.widget.Button;

import com.twt.service.base.BaseActivity;


public class MainActivity extends BaseActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(v -> test2());

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(v -> test());
    }

    private void test() {


    }


    private void test2() {
//        Intent intent = new Intent(this, BikeActivity.class);
//        startActivity(intent);
    }

}
