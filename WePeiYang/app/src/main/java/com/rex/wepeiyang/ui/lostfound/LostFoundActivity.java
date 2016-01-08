package com.rex.wepeiyang.ui.lostfound;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.support.ResourceHelper;
import com.rex.wepeiyang.support.StatusBarHelper;
import com.rex.wepeiyang.ui.BaseActivity;
import com.rex.wepeiyang.ui.common.TabFragmentAdapter;
import com.rex.wepeiyang.ui.lostfound.found.FoundFragment;
import com.rex.wepeiyang.ui.lostfound.lost.LostFragment;
import com.rex.wepeiyang.ui.lostfound.post.PostLostFoundActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Rex on 2015/8/1.
 */
public class LostFoundActivity extends BaseActivity implements LostFoundView {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tbl_lostfound)
    TabLayout tblLostfound;
    @InjectView(R.id.vp_lostfound)
    ViewPager vpLostfound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTab();
        getWindow().setStatusBarColor(getResources().getColor(R.color.lost_found_primary_color));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lostfound, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.icon_lost_found_personal:
                PostLostFoundActivity.actionStart(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        StatusBarHelper.setStatusBar(this, ResourceHelper.getColor(R.color.lost_found_dark_primary_color));
    }

    @Override
    public void initTab() {
        List<String> tabList = new ArrayList<>();
        tabList.add("丢了东西");
        tabList.add("捡了东西");
        tblLostfound.addTab(tblLostfound.newTab().setText(tabList.get(0)));
        tblLostfound.addTab(tblLostfound.newTab().setText(tabList.get(1)));
        tblLostfound.setupWithViewPager(vpLostfound);
        List<Fragment> fragmentList = new ArrayList<>();
        LostFragment lostFragment = new LostFragment();
        FoundFragment foundFragment = new FoundFragment();
        fragmentList.add(lostFragment);
        fragmentList.add(foundFragment);
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        vpLostfound.setAdapter(adapter);
        tblLostfound.setTabsFromPagerAdapter(adapter);
    }
}
