package com.twtstudio.tjliqy.party.ui.inquiry;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.twtstudio.tjliqy.party.ui.inquiry.Score20.InquiryPageScore20Fragment;
import com.twtstudio.tjliqy.party.ui.inquiry.other.InquiryOtherFragment;

/**
 * Created by dell on 2016/7/19.
 */
public class InquiryViewPageAdapter extends FragmentPagerAdapter {

    private Context context;

    private String tabTitles[] = new String[]{"20课时成绩","结业考试成绩","院级积极分子成绩","预备党员党校成绩"};

    private static  final int PAGE_COUNT = 4;

    public InquiryViewPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return InquiryPageScore20Fragment.newInstance(1);
        }
        return InquiryOtherFragment.newInstance(position + 1);
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
