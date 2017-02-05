package com.twtstudio.retrox.bike.bike.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.bike.BikeFragment;
import com.twtstudio.retrox.bike.bike.ui.announcement.AnnouncementFragment;
import com.twtstudio.retrox.bike.bike.ui.data.HomeFragment;
import com.twtstudio.retrox.bike.common.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by jcy on 16-8-20.
 */
public class BikeActivity extends AppCompatActivity {
    @BindView(R2.id.toolbar_main_act)
    Toolbar mToolBar;
    @BindView(R2.id.tab_main_act)
    TabLayout mTabLayout;
    @BindView(R2.id.view_pager_main)
    MyViewPager mViewPager;

    private Unbinder mUnbinder;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BikeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_main);
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(view -> onBackPressed());
        setTitle("自行车");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bike_toolbar_color));
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragement(new BikeFragment());
        viewPagerAdapter.addFragement(new HomeFragment());
        viewPagerAdapter.addFragement(new AnnouncementFragment());
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.requestDisallowInterceptTouchEvent(false);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayout.Tab tab = mTabLayout.getTabAt(0);
        tab.setIcon(R.drawable.point_layout_map);
        TabLayout.Tab tab1=mTabLayout.getTabAt(1);
        tab1.setIcon(R.drawable.use_record);
        TabLayout.Tab tab2=mTabLayout.getTabAt(2);
        tab2.setIcon(R.drawable.announcement);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"自行车定位服务需要权限",Toast.LENGTH_SHORT);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE},0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragement(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //null
        }
    }
}
