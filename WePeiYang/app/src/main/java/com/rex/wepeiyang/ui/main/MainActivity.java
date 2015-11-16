package com.rex.wepeiyang.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.rex.wepeiyang.R;
import com.rex.wepeiyang.ui.gpa.GpaActivity;
import com.rex.wepeiyang.ui.library.LibraryActivity;
import com.rex.wepeiyang.ui.news.NewsActivity;
import com.rex.wepeiyang.ui.notice.NoticeActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {

    @InjectView(R.id.dl_main)
    DrawerLayout dlMain;
    @InjectView(R.id.btn_study_room_query)
    LinearLayout btnStudyRoomQuery;
    @InjectView(R.id.btn_gpa_query)
    LinearLayout btnGpaQuery;
    @InjectView(R.id.btn_library_query)
    LinearLayout btnLibraryQuery;
    @InjectView(R.id.drawer)
    NavigationView drawer;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.slider)
    SliderLayout slider;
    @InjectView(R.id.indicator)
    PagerIndicator indicator;
    @InjectView(R.id.rl_more_campus_news)
    LinearLayout rlMoreCampusNews;
    @InjectView(R.id.rl_more_campus_note)
    LinearLayout rlMoreCampusNote;
    @InjectView(R.id.rl_more_jobs_info)
    LinearLayout rlMoreJobsInfo;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, dlMain, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        dlMain.setDrawerListener(drawerToggle);
        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_account_settings:
                        break;
                    case R.id.item_give_advice:
                        break;
                    case R.id.item_share_app:
                        break;
                    case R.id.item_check_update:
                        break;
                }
                dlMain.closeDrawers();
                return true;
            }
        });
        btnGpaQuery.setOnClickListener(this);
        btnLibraryQuery.setOnClickListener(this);
        btnStudyRoomQuery.setOnClickListener(this);
        rlMoreCampusNews.setOnClickListener(this);
        rlMoreCampusNote.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_study_room_query:
                break;
            case R.id.btn_gpa_query:
                GpaActivity.actionStart(this);
                break;
            case R.id.btn_library_query:
                LibraryActivity.actionStart(this);
                break;
            case R.id.rl_more_campus_news:
                NewsActivity.actionStart(this);
                break;
            case R.id.rl_more_campus_note:
                NoticeActivity.actionStart(this);
        }
    }
}
