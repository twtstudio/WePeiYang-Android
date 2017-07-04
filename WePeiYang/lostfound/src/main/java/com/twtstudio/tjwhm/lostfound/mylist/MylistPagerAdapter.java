package com.twtstudio.tjwhm.lostfound.mylist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjwhm on 2017/7/4.
 **/

public class MylistPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentsOfMylist = new ArrayList<>();
    List<String> fragmentsTitles = new ArrayList<>();
    public MylistPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void add(Fragment fragment,String title){
        fragmentsOfMylist.add(fragment);
        fragmentsTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsOfMylist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsOfMylist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsTitles.get(position);
    }
}
