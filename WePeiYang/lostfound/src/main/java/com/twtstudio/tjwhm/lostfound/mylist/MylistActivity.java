package com.twtstudio.tjwhm.lostfound.mylist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.R2;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class MylistActivity extends BaseActivity {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.mylist_pager)
    ViewPager mylist_pager;
    @BindView(R2.id.mylist_tabLayout)
    TabLayout mylist_tabLayout;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.lf_activity_mylist;
    }

    @Override
    protected Toolbar getToolbarView() {
        toolbar.setTitle("我的");
        return toolbar;
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MylistPagerAdapter mylistPagerAdapter = new MylistPagerAdapter(getSupportFragmentManager());
        mylistPagerAdapter.add(MylistFragment.newInstance("lost"),"我丢失的");
        mylistPagerAdapter.add(MylistFragment.newInstance("found"),"我捡到的");
        mylist_pager.setAdapter(mylistPagerAdapter);
        mylist_tabLayout.setupWithViewPager(mylist_pager);
        mylist_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mylist_tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"));
    }


}
