package com.twtstudio.tjwhm.lostfound.waterfall;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.twtstudio.retrox.auth.login.LoginActivity;
import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.twtstudio.tjwhm.lostfound.mylist.MylistActivity;
import com.twtstudio.tjwhm.lostfound.release.ReleaseActivity;
import com.twtstudio.tjwhm.lostfound.search.SearchActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class WaterfallActivity extends BaseActivity {
    @BindView(R.id.waterfall_type_recyclerview)
    RecyclerView waterfall_type_recyclerview;
    @BindView(R.id.waterfall_type_grey)
    ImageView waterfall_type_grey;
    @BindView(R.id.waterfall_type_blue)
    ImageView waterfall_type_blue;
    @BindView(R.id.waterfall_cardview_types)
    CardView waterfall_cardview_types;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.waterfall_tabLayout)
    TabLayout waterfall_tabLayout;
    @BindView(R.id.waterfall_pager)
    ViewPager waterfall_pager;
    @BindView(R.id.waterfall_fab_login)
    FloatingActionButton waterfall_fab_login;
    @BindView(R.id.waterfall_fab_menu)
    FloatingActionMenu waterfall_fab_menu;
    @BindView(R.id.waterfall_fab_lost)
    FloatingActionButton waterfall_fab_lost;
    @BindView(R.id.waterfall_fab_found)
    FloatingActionButton waterfall_fab_found;
    @BindView(R.id.waterfall_types_all)
    TextView waterfall_types_all;
    @BindView(R.id.waterfall_cover)
    TextView waterfall_cover;
    @BindView(R.id.waterfall_type)
    RelativeLayout waterfall_type;
    int type = -1;
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    WaterfallFragment lostFragment;
    WaterfallFragment foundFragment;

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
            waterfall_fab_menu.close(true);
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
        lostFragment = WaterfallFragment.newInstance("lost");
        foundFragment = WaterfallFragment.newInstance("found");
        WaterfallPagerAdapter waterfallPagerAdapter = new WaterfallPagerAdapter(getSupportFragmentManager());
        waterfallPagerAdapter.add(lostFragment, "丢失");
        waterfallPagerAdapter.add(foundFragment, "捡到");
        waterfall_pager.setAdapter(waterfallPagerAdapter);
        waterfall_tabLayout.setupWithViewPager(waterfall_pager);
        waterfall_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        waterfall_tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"));
        waterfall_type_recyclerview.setLayoutManager(layoutManager);
        waterfall_type_blue.setVisibility(View.GONE);
        waterfall_cardview_types.setVisibility(View.GONE);
        waterfall_cover.setVisibility(View.GONE);
    }

    public void setWaterfallType(int type) {
        lostFragment.loadWaterfallDataWithType(type);
        foundFragment.loadWaterfallDataWithType(type);
        waterfall_type_recyclerview.setAdapter(new WaterfallTypeTableAdapter(this, this, type));
        if (type == -1) {
            waterfall_types_all.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            waterfall_types_all.setTypeface(Typeface.DEFAULT);
        }
    }

    @OnClick({R.id.waterfall_type_blue, R.id.waterfall_fab_login,
            R.id.waterfall_fab_found, R.id.waterfall_fab_lost,
            R.id.waterfall_type_grey, R.id.waterfall_types_all,
            R.id.waterfall_cover, R.id.waterfall_type})
    public void submit(View view) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        if (view == waterfall_fab_login) {
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            waterfall_fab_menu.close(true);
        } else if (view == waterfall_fab_lost) {
            bundle.putString("lostOrFound", "lost");
            intent.putExtras(bundle);
            intent.setClass(this, ReleaseActivity.class);
            startActivity(intent);
            waterfall_fab_menu.close(true);
        } else if (view == waterfall_fab_found) {
            bundle.putString("lostOrFound", "found");
            intent.putExtras(bundle);
            intent.setClass(this, ReleaseActivity.class);
            startActivity(intent);
            waterfall_fab_menu.close(true);
        } else if (view == waterfall_type_grey) {
            waterfall_type_blue.setVisibility(View.VISIBLE);
            waterfall_type_grey.setVisibility(View.GONE);
            waterfall_cardview_types.setVisibility(View.VISIBLE);
            waterfall_cover.setVisibility(View.VISIBLE);
        } else if (view == waterfall_type_blue) {
            waterfall_type_grey.setVisibility(View.VISIBLE);
            waterfall_type_blue.setVisibility(View.GONE);
            waterfall_cardview_types.setVisibility(View.GONE);
            waterfall_cover.setVisibility(View.GONE);
        } else if (view == waterfall_types_all) {
            setWaterfallType(-1);
        } else if (view == waterfall_cover) {
            waterfall_cover.setVisibility(View.GONE);
            waterfall_type_grey.setVisibility(View.VISIBLE);
            waterfall_type_blue.setVisibility(View.GONE);
            waterfall_cardview_types.setVisibility(View.GONE);
            waterfall_fab_menu.close(true);
        } else if (view == waterfall_type) {
            if (waterfall_type_grey.getVisibility() == View.VISIBLE) {
                waterfall_type_blue.setVisibility(View.VISIBLE);
                waterfall_type_grey.setVisibility(View.GONE);
                waterfall_cardview_types.setVisibility(View.VISIBLE);
                waterfall_cover.setVisibility(View.VISIBLE);
            } else if (waterfall_type_blue.getVisibility() == View.VISIBLE) {
                waterfall_type_grey.setVisibility(View.VISIBLE);
                waterfall_type_blue.setVisibility(View.GONE);
                waterfall_cardview_types.setVisibility(View.GONE);
                waterfall_cover.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        waterfall_cardview_types.setVisibility(View.GONE);
    }
}
