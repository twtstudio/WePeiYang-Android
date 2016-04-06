package com.twt.service.ui.lostfound.post.mypost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.twt.service.R;
import com.twt.service.ui.common.TabFragmentAdapter;
import com.twt.service.ui.lostfound.post.mypost.myfound.MyFoundFragment;
import com.twt.service.ui.lostfound.post.mypost.mylost.MyLostFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyLostFoundActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tbl_my_lost_found)
    TabLayout tblMyLostFound;
    @InjectView(R.id.vp_my_lost_found)
    ViewPager vpMyLostFound;
    private MyLostFragment mMyLostFragment;
    private MyFoundFragment mMyFoundFragment;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyLostFoundActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lost_found);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTab();
    }

    private void initTab() {
        List<String> tabs = new ArrayList<>();
        tabs.add("我丢了");
        tabs.add("我捡到");
        tblMyLostFound.addTab(tblMyLostFound.newTab().setText(tabs.get(0)));
        tblMyLostFound.addTab(tblMyLostFound.newTab().setText(tabs.get(1)));
        tblMyLostFound.setTabMode(TabLayout.MODE_FIXED);
        List<Fragment> fragments = new ArrayList<>();
        mMyLostFragment = new MyLostFragment();
        mMyFoundFragment = new MyFoundFragment();
        fragments.add(mMyLostFragment);
        fragments.add(mMyFoundFragment);
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments, tabs);
        vpMyLostFound.setAdapter(tabFragmentAdapter);
        tblMyLostFound.setupWithViewPager(vpMyLostFound);
        tblMyLostFound.setTabsFromPagerAdapter(tabFragmentAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
