package com.twtstudio.tjwhm.lostfound.waterfall;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class WaterfallActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.waterfall_tabLayout)
    TabLayout waterfall_tabLayout;
    @BindView(R.id.waterfall_pager)
    ViewPager waterfall_pager;



    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_waterfall;
    }

    @Override
    protected Toolbar getToolbarView() {
        return toolbar;
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        WaterfallPagerAdapter waterfallPagerAdapter = new WaterfallPagerAdapter(getSupportFragmentManager());
        waterfallPagerAdapter.add(WaterfallFragment.newInstance("lost"), "丢失");
        waterfallPagerAdapter.add(WaterfallFragment.newInstance("found"), "捡到");
        waterfall_pager.setAdapter(waterfallPagerAdapter);
        waterfall_tabLayout.setupWithViewPager(waterfall_pager);
        waterfall_tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
    }
}
