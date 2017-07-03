package com.twtstudio.tjwhm.lostfound.serach;

import android.support.v7.widget.Toolbar;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class SearchActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_search;
    }

    @Override
    protected Toolbar getToolbarView() {
        toolbar.setTitle("搜索");
        return toolbar;
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected int getToolbarMenu() {
        return 0;
    }
}
