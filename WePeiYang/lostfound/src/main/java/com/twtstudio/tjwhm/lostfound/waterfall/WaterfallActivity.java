package com.twtstudio.tjwhm.lostfound.waterfall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.twtstudio.retrox.auth.login.LoginActivity;
import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.twtstudio.tjwhm.lostfound.mylist.MylistActivity;
import com.twtstudio.tjwhm.lostfound.release.ReleaseActivity;
import com.twtstudio.tjwhm.lostfound.search.SearchActivity;

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
    @BindView(R.id.waterfall_fab_lost)
    FloatingActionButton waterfall_fab_lost;
    @BindView(R.id.waterfall_fab_found)
    FloatingActionButton waterfall_fab_found;
    @BindView(R.id.waterfall_fab_login)
    FloatingActionButton waterfall_fab_login;
    @BindView(R.id.waterfall_fab_menu)
    FloatingActionsMenu waterfall_fab_menu;

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
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            waterfall_fab_menu.collapse();
            if (itemId == R.id.waterfall_search) {
                Intent intent = new Intent();
                intent.setClass(WaterfallActivity.this, SearchActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.waterfall_indi) {
                Intent intent = new Intent();
                intent.setClass(WaterfallActivity.this, MylistActivity.class);
                startActivity(intent);
            }
            return false;
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

        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        waterfall_fab_lost.setOnClickListener(view -> {
            bundle.putString("lostOrFound", "lost");
            intent.putExtras(bundle);
            intent.setClass(WaterfallActivity.this, ReleaseActivity.class);
            startActivity(intent);
            waterfall_fab_menu.collapse();
        });
        waterfall_fab_found.setOnClickListener(view -> {
            bundle.putString("lostOrFound", "found");
            intent.putExtras(bundle);
            intent.setClass(WaterfallActivity.this, ReleaseActivity.class);
            startActivity(intent);
            waterfall_fab_menu.collapse();
        });
        waterfall_fab_login.setOnClickListener(view -> {
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            waterfall_fab_menu.collapse();
        });
    }

}
