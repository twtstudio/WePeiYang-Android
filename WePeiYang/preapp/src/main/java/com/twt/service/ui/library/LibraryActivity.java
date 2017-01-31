package com.twt.service.ui.library;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.twt.service.R;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.library.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class LibraryActivity extends BaseActivity implements LibraryView{
    @InjectView(R.id.tl_library)
    TabLayout tlLibrary;
    @InjectView(R.id.vp_library)
    ViewPager vpLibrary;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private ViewPagerAdapter adapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LibraryActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.library_primary_color));
        }
        adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SearchFragment.newInstance());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initTab() {
        tlLibrary.addTab(tlLibrary.newTab().setText("我的账户"));
        tlLibrary.addTab(tlLibrary.newTab().setText("查找书籍"));
        tlLibrary.setupWithViewPager(vpLibrary);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        List<Fragment> fragmentList=new ArrayList<>();
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        void addFragment(Fragment fragment)
        {
            fragmentList.add(fragment);
        }
    }
}
