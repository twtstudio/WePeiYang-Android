package com.twtstudio.tjwhm.lostfound.release;

import android.support.v7.widget.Toolbar;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class ReleaseActivity extends BaseActivity{
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_release;
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
