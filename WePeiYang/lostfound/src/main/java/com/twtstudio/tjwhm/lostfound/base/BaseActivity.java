package com.twtstudio.tjwhm.lostfound.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;

import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.BaseView {

    private Toolbar toolbar;

    protected abstract int getLayoutResourceId();

    protected abstract Toolbar getToolbarView();

    protected abstract boolean isShowBackArrow();

    protected int getToolbarMenu() {
        return 0;
    }

    protected void setToolbarMenuClickEvent() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getToolbarMenu() == 0) {
            return false;
        }
        getMenuInflater().inflate(getToolbarMenu(), menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        toolbar = getToolbarView();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (isShowBackArrow() && getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(view -> onBackPressed());
            }
        }
        setToolbarMenuClickEvent();

    }
}
