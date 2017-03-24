package com.twtstudio.tjliqy.party.ui.notification;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.ui.BaseActivity;

import butterknife.BindView;

/**
 * Created by dell on 2016/7/19.
 */
public class NotificationActivity extends BaseActivity {


    NotificationViewPagerAdapter pagerAdapter;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.tl_notification)
    TabLayout tabLayout;
    @BindView(R2.id.vp_notification)
    ViewPager viewPager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_notification;
    }

    @Override
    public void preInitView() {
        pagerAdapter = new NotificationViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void initView() {

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle(R.string.notification_party);
        return toolbar;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getMenu() {
        return 0;
    }

}
