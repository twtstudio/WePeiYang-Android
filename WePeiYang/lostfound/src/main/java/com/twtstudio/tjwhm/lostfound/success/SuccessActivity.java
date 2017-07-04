package com.twtstudio.tjwhm.lostfound.success;

import android.support.v7.widget.Toolbar;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class SuccessActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_success;
    }

    @Override
    protected Toolbar getToolbarView() {
        toolbar.setTitle("发布/分享");
        return toolbar;
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }


}
