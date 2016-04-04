package com.twt.service.ui.lostfound.post.lost.event;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.twt.service.R;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.common.TabFragmentAdapter;
import com.twt.service.ui.lostfound.LostFoundView;
import com.twt.service.ui.lostfound.found.FoundFragment;
import com.twt.service.ui.lostfound.lost.LostFragment;
import com.twt.service.ui.lostfound.post.PostLostFoundActivity;

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
    @InjectView(R.id.fab_lost_found)
    FloatingActionButton fabLostFound;
    private int page;

    public static void actionStart(Context context, int page) {
        Intent intent = new Intent(context, LostFoundActivity.class);
        intent.putExtra("page", page);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTab();
        page = getIntent().getIntExtra("page", 0);
        vpLostfound.setCurrentItem(page);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.lost_found_primary_color));
        }
        fabLostFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostLostFoundActivity.actionStart(LostFoundActivity.this);
            }
        });
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
    public void initTab() {
        List<String> tabList = new ArrayList<>();
        tabList.add("丢失");
        tabList.add("捡到");
        tblLostfound.setTabMode(TabLayout.MODE_FIXED);
        tblLostfound.addTab(tblLostfound.newTab().setText(tabList.get(0)));
        tblLostfound.addTab(tblLostfound.newTab().setText(tabList.get(1)));
        List<Fragment> fragmentList = new ArrayList<>();
        LostFragment lostFragment = new LostFragment();
        FoundFragment foundFragment = new FoundFragment();
        fragmentList.add(lostFragment);
        fragmentList.add(foundFragment);
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        vpLostfound.setAdapter(adapter);
        tblLostfound.setTabsFromPagerAdapter(adapter);
        tblLostfound.setupWithViewPager(vpLostfound);

    }
}
