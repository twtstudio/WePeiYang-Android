package com.twt.service.rxsrc.read.home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseActivity;
import com.twt.service.rxsrc.read.home.profile.BookProfileFragment;
import com.twt.service.rxsrc.read.home.recommend.BookRecommendController;
import com.twt.service.rxsrc.read.home.recommend.BookRecommendFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by tjliqy on 2016/10/27.
 */

public class BookHomeActivity extends BaseActivity implements BookRecommendController {


    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tl_inquiry)
    TabLayout mTlInquiry;
    @InjectView(R.id.vp_main)
    ViewPager mVpMain;


    @Override
    protected int getLayout() {
        return R.layout.activity_book_main;
    }

    @Override
    protected void actionStart(Context context) {

    }

    @Override
    protected int getStatusbarColor() {
        return R.color.read_primary_color;
    }

    @Override
    protected Toolbar getToolbar() {
        mToolbar.setTitle(R.string.read);
        return mToolbar;
    }


    @Override
    protected void initView() {

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        mViewPagerAdapter.addFragement(new BookRecommendFragment());
        mViewPagerAdapter.addFragement(new BookProfileFragment());
        mVpMain.setAdapter(mViewPagerAdapter);
        mTlInquiry.setupWithViewPager(mVpMain);
        mTlInquiry.setTabMode(TabLayout.MODE_FIXED);
    }

    public static void onActionStart(Context context){
        Intent intent = new Intent(context, BookHomeActivity.class);
        context.startActivity(intent);
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;


        private List<Fragment> fragmentList = new ArrayList<>();


        public void addFragement(Fragment fragment) {
            fragmentList.add(fragment);
        }

        private String tabTitles[] = new String[]{"推荐","我的"};


        public ViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
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
            return tabTitles[position];
        }
    }
}
