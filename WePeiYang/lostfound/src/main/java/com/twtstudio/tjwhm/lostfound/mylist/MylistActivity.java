package com.twtstudio.tjwhm.lostfound.mylist;

import android.support.v7.widget.Toolbar;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class MylistActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_mylist;
    }

    @Override
    protected Toolbar getToolbarView() {
        return null;
    }

    @Override
    protected boolean isShowBackArrow() {
        return false;
    }

    @Override
    protected int getToolbarMenu() {
        return 0;
    }
}
