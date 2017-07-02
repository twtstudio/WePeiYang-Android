package com.twtstudio.tjwhm.lostfound.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.BaseView {

    protected abstract int getLayoutResourceId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
    }
}
