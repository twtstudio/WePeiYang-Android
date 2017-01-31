package com.twt.service.party.ui.inquiry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.twt.service.R;
import com.twt.service.party.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell on 2016/7/19.
 */
public class InquiryActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tl_inquiry)
    TabLayout tabLayout;
    @InjectView(R.id.vp_inquiry)
    ViewPager viewPager;
    private InquiryViewPageAdapter pagerAdapter;


    @Override
    public int getContentViewId() {
        return R.layout.activity_party_inquiry;
    }

    @Override
    public void preInitView() {

    }

    @Override
    public void initView() {
        pagerAdapter = new InquiryViewPageAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle(R.string.inquiry_party);
        return toolbar;
    }

    @Override
    public int getMenu() {
        return 0;
    }


    public static void actionStart(Context context){
        Intent intent = new Intent(context,InquiryActivity.class);
        context.startActivity(intent);
    }
}
