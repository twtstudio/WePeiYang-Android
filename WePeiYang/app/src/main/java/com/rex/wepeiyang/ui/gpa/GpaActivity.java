package com.rex.wepeiyang.ui.gpa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.ui.BaseActivity;
import com.rex.wepeiyang.ui.common.TabFragmentAdapter;
import com.rex.wepeiyang.ui.gpa.analysis.AnalysisFragment;
import com.rex.wepeiyang.ui.gpa.score.ScoreFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class GpaActivity extends BaseActivity implements GpaView {
    @InjectView(R.id.tbl_gpa)
    TabLayout tblGpa;
    @InjectView(R.id.vp_gpa)
    ViewPager vpGpa;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GpaActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initTab();
    }


    @Override
    public void initTab() {
        List<String> tabList = new ArrayList<>();
        tabList.add("我的成绩单");
        tabList.add("成绩分析");
        tblGpa.addTab(tblGpa.newTab().setText(tabList.get(0)));
        tblGpa.addTab(tblGpa.newTab().setText(tabList.get(1)));
        List<Fragment> fragmentList = new ArrayList<>();
        ScoreFragment scoreFragment = new ScoreFragment();
        AnalysisFragment analysisFragment = new AnalysisFragment();
        fragmentList.add(scoreFragment);
        fragmentList.add(analysisFragment);
        TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        vpGpa.setAdapter(fragmentAdapter);
        tblGpa.setupWithViewPager(vpGpa);
        tblGpa.setTabsFromPagerAdapter(fragmentAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                GpaActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
