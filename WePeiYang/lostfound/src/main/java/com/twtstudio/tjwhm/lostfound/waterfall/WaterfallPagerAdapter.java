package com.twtstudio.tjwhm.lostfound.waterfall;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjwhm on 2017/7/3.
 **/

public class WaterfallPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentsOfWaterfall = new ArrayList<>();
    private List<String> fragmentsTitles = new ArrayList<>();

    public WaterfallPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(Fragment fragment, String title){
        fragmentsOfWaterfall.add(fragment);
        fragmentsTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsOfWaterfall.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsOfWaterfall.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsTitles.get(position);
    }
}
