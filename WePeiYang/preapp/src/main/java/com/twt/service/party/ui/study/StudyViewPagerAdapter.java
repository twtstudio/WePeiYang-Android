package com.twt.service.party.ui.study;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by dell on 2016/7/19.
 */
public class StudyViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private String tabTitles[] = new String[]{"网上党校","预备党员党校"};

    //,"支部书记培训班","面向全体党员"暂时不存在

    private static  final int PAGE_COUNT = 2;

    public StudyViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return StudyPageFragment.newInstance(position + 1);
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