package com.twt.service.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.twt.service.R;
import com.twt.service.common.ui.BaseActivity;
import com.twt.service.support.ResourceHelper;
import com.twt.service.ui.common.TabFragmentAdapter;
import com.twt.service.ui.news.associationsnews.AssociationFragment;
import com.twt.service.ui.news.collegenews.CollegeNewsFragment;
import com.twt.service.ui.news.importantnews.ImportantNewsFragment;
import com.twt.service.ui.news.viewpoint.ViewPointFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tbl_news)
    TabLayout tblNews;
    @InjectView(R.id.vp_news)
    ViewPager vpNews;


    @Override
    protected int getLayout() {
        return R.layout.activity_news;
    }

    @Override
    protected void actionStart(Context context) {
        Intent intent = new Intent(context, NewsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getStatusbarColor() {
        return R.color.news_dark_primary_color;
    }


    @Override
    protected void initView() {
        List<String> tabs = new ArrayList<>();
        tabs.add(ResourceHelper.getString(R.string.important_news));
        tabs.add(ResourceHelper.getString(R.string.viewpoint));
        tabs.add(ResourceHelper.getString(R.string.association_news));
        tabs.add(ResourceHelper.getString(R.string.college_news));
        tblNews.addTab(tblNews.newTab().setText(tabs.get(0)));
        tblNews.addTab(tblNews.newTab().setText(tabs.get(1)));
        tblNews.addTab(tblNews.newTab().setText(tabs.get(2)));
        tblNews.addTab(tblNews.newTab().setText(tabs.get(3)));
        tblNews.setTabMode(TabLayout.MODE_FIXED);
        List<Fragment> fragmentList = new ArrayList<>();
        ImportantNewsFragment importantNewsFragment = new ImportantNewsFragment();
        ViewPointFragment viewPointFragment = new ViewPointFragment();
        AssociationFragment associationFragment = new AssociationFragment();
        CollegeNewsFragment collegeNewsFragment = new CollegeNewsFragment();
        fragmentList.add(importantNewsFragment);
        fragmentList.add(viewPointFragment);
        fragmentList.add(associationFragment);
        fragmentList.add(collegeNewsFragment);
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabs);
        vpNews.setAdapter(adapter);
        tblNews.setupWithViewPager(vpNews);
        tblNews.setTabsFromPagerAdapter(adapter);
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }
}
