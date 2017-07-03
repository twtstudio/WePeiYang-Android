package com.twtstudio.tjwhm.lostfound.serach;

import android.support.v7.widget.Toolbar;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class SearchActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_search;
    }

    @Override
    protected Toolbar getToolbarView() {
        return null;
    }

    @Override
    protected boolean isShowBackArrow() {
        return false;
    }
}
