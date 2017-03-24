package com.twtstudio.tjliqy.party.ui.inquiry;

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
public class InquiryActivity extends BaseActivity {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.tl_inquiry)
    TabLayout tabLayout;
    @BindView(R2.id.vp_inquiry)
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
