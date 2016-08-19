package com.twt.service.party.ui.study;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.twt.service.R;
import com.twt.service.party.ui.BaseActivity;

import butterknife.InjectView;

/**
 * Created by dell on 2016/7/19.
 */
public class StudyActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tl_study)
    TabLayout tabLayout;
    @InjectView(R.id.vp_study)
    ViewPager viewPager;

    private StudyViewPagerAdapter pagerAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_study;
    }

    @Override
    public void preInitView() {

    }

    @Override
    public void initView() {
        pagerAdapter = new StudyViewPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle(R.string.study_party);
        return toolbar;
    }

    @Override
    public int getMenu() {
        return 0;
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,StudyActivity.class);
        context.startActivity(intent);
    }
}
