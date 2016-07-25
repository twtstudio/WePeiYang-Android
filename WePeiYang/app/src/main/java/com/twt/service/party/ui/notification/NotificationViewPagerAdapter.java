package com.twt.service.party.ui.notification;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by dell on 2016/7/19.
 */
public class NotificationViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    private String tabTitles[] = new String[]{"全部公告","申请人党校","院级积极分子党校","预备党员党校","活动通知"};

    private static  final int PAGE_COUNT = 5;

    public NotificationViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return NotificationPageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
