package com.rex.wepeiyang.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.ui.BaseActivity;
import com.rex.wepeiyang.ui.common.TabFragmentAdapter;
import com.rex.wepeiyang.ui.news.associationsnews.AssociationFragment;
import com.rex.wepeiyang.ui.news.collegenews.CollegeNewsFragment;
import com.rex.wepeiyang.ui.news.importantnews.ImportantNewsFragment;
import com.rex.wepeiyang.ui.news.viewpoint.ViewPointFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsActivity extends BaseActivity implements NewsView {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tbl_news)
    TabLayout tblNews;
    @InjectView(R.id.vp_news)
    ViewPager vpNews;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NewsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initTab() {
        List<String> tabs = new ArrayList<>();
        tabs.add("天大要闻");
        tabs.add("视点观察");
        tabs.add("社团风采");
        tabs.add("院系动态");
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
}
