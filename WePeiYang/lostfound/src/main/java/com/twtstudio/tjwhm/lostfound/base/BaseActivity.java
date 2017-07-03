package com.twtstudio.tjwhm.lostfound.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.BaseView {

    private Toolbar toolbar;

    protected abstract int getLayoutResourceId();

    protected abstract Toolbar getToolbarView();

    protected abstract boolean isShowBackArrow();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        toolbar = getToolbarView();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (isShowBackArrow() && getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }
}
