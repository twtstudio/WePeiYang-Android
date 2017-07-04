package com.twtstudio.tjwhm.lostfound.waterfall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.twtstudio.tjwhm.lostfound.mylist.MylistActivity;
import com.twtstudio.tjwhm.lostfound.success.SuccessActivity;

import butterknife.BindView;

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
        toolbar.setTitle("失物招领");
        return toolbar;
    }

    @Override
    protected void setToolbarMenuClickEvent() {
        super.setToolbarMenuClickEvent();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.waterfall_search) {
                    Intent intent = new Intent();
                    intent.setClass(WaterfallActivity.this, SuccessActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.waterfall_indi) {
                    Intent intent = new Intent();
                    intent.setClass(WaterfallActivity.this, MylistActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected int getToolbarMenu() {
        return R.menu.waterfall_menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WaterfallPagerAdapter waterfallPagerAdapter = new WaterfallPagerAdapter(getSupportFragmentManager());
        waterfallPagerAdapter.add(WaterfallFragment.newInstance("lost"), "丢失");
        waterfallPagerAdapter.add(WaterfallFragment.newInstance("found"), "捡到");
        waterfall_pager.setAdapter(waterfallPagerAdapter);
        waterfall_tabLayout.setupWithViewPager(waterfall_pager);
        waterfall_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        waterfall_tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"));


    }
}
