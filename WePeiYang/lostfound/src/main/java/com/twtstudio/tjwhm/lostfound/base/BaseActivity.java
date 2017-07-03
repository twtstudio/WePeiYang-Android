package com.twtstudio.tjwhm.lostfound.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;

import com.twtstudio.tjwhm.lostfound.R;

import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.BaseView {

    private Toolbar toolbar;

    protected abstract int getLayoutResourceId();

    protected abstract Toolbar getToolbarView();

    protected abstract boolean isShowBackArrow();

    protected abstract int getToolbarMenu();

    protected  void setToolbarMenuClickEvent(){};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getToolbarMenu(),menu);
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
                if (getSupportActionBar() == null) {
                    System.out.println("BaseActivity.onCreate" + "abcdef");
                }
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        setToolbarMenuClickEvent();
    }
}
