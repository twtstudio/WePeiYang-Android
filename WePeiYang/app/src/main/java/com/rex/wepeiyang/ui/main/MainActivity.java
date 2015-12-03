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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.Main;
import com.rex.wepeiyang.interactor.MainInteractor;
import com.rex.wepeiyang.interactor.MainInteractorImpl;
import com.rex.wepeiyang.ui.gpa.GpaActivity;
import com.rex.wepeiyang.ui.library.LibraryActivity;
import com.rex.wepeiyang.ui.news.NewsActivity;
import com.rex.wepeiyang.ui.news.details.NewsDetailsActivity;
import com.rex.wepeiyang.ui.notice.NoticeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener, MainView {

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
    @InjectView(R.id.pb_main)
    ProgressBar pbMain;
    @InjectView(R.id.tv_campusnews_title1)
    TextView tvCampusnewsTitle1;
    @InjectView(R.id.tv_campusnews_viewcount1)
    TextView tvCampusnewsViewcount1;
    @InjectView(R.id.tv_campusnews_comment1)
    TextView tvCampusnewsComment1;
    @InjectView(R.id.tv_campusnews_date1)
    TextView tvCampusnewsDate1;
    @InjectView(R.id.tv_campusnews_title2)
    TextView tvCampusnewsTitle2;
    @InjectView(R.id.tv_campusnews_viewcount2)
    TextView tvCampusnewsViewcount2;
    @InjectView(R.id.tv_campusnews_comment2)
    TextView tvCampusnewsComment2;
    @InjectView(R.id.tv_campusnews_date2)
    TextView tvCampusnewsDate2;
    @InjectView(R.id.tv_campusnews_title3)
    TextView tvCampusnewsTitle3;
    @InjectView(R.id.tv_campusnews_viewcount3)
    TextView tvCampusnewsViewcount3;
    @InjectView(R.id.tv_campusnews_comment3)
    TextView tvCampusnewsComment3;
    @InjectView(R.id.tv_campusnews_date3)
    TextView tvCampusnewsDate3;
    @InjectView(R.id.tv_announcement_title1)
    TextView tvAnnouncementTitle1;
    @InjectView(R.id.tv_announcement_comefrom1)
    TextView tvAnnouncementComefrom1;
    @InjectView(R.id.tv_announcement_date1)
    TextView tvAnnouncementDate1;
    @InjectView(R.id.tv_announcement_title2)
    TextView tvAnnouncementTitle2;
    @InjectView(R.id.tv_announcement_comefrom2)
    TextView tvAnnouncementComefrom2;
    @InjectView(R.id.tv_announcement_date2)
    TextView tvAnnouncementDate2;
    @InjectView(R.id.tv_announcement_title3)
    TextView tvAnnouncementTitle3;
    @InjectView(R.id.tv_announcement_comefrom3)
    TextView tvAnnouncementComefrom3;
    @InjectView(R.id.tv_announcement_date3)
    TextView tvAnnouncementDate3;
    @InjectView(R.id.tv_jobs_title1)
    TextView tvJobsTitle1;
    @InjectView(R.id.tv_jobs_date1)
    TextView tvJobsDate1;
    @InjectView(R.id.tv_jobs_title2)
    TextView tvJobsTitle2;
    @InjectView(R.id.tv_jobs_date2)
    TextView tvJobsDate2;
    @InjectView(R.id.tv_jobs_title3)
    TextView tvJobsTitle3;
    @InjectView(R.id.tv_jobs_date3)
    TextView tvJobsDate3;
    @InjectView(R.id.campusnews1)
    LinearLayout campusnews1;
    @InjectView(R.id.campusnews2)
    LinearLayout campusnews2;
    @InjectView(R.id.campusnews3)
    LinearLayout campusnews3;
    @InjectView(R.id.announcement1)
    LinearLayout announcement1;
    @InjectView(R.id.announcement2)
    LinearLayout announcement2;
    @InjectView(R.id.announcement3)
    LinearLayout announcement3;
    @InjectView(R.id.jobs1)
    LinearLayout jobs1;
    @InjectView(R.id.jobs2)
    LinearLayout jobs2;
    @InjectView(R.id.jobs3)
    LinearLayout jobs3;

    private MainPresenter presenter;

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
        presenter = new MainPresenterImpl(this, new MainInteractorImpl());
        presenter.loadData();
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
                break;
        }
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        pbMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbMain.setVisibility(View.GONE);
    }

    @Override
    public void bindData(Main main) {
        final List<Main.Data.News.Annoucement> annoucements = new ArrayList<>();
        annoucements.addAll(main.data.news.annoucements);
        tvAnnouncementTitle1.setText(annoucements.get(0).subject);
        tvAnnouncementComefrom1.setText(annoucements.get(0).gonggao);
        tvAnnouncementDate1.setText(annoucements.get(0).addat);
        tvAnnouncementTitle2.setText(annoucements.get(1).subject);
        tvAnnouncementComefrom2.setText(annoucements.get(1).gonggao);
        tvAnnouncementDate2.setText(annoucements.get(1).addat);
        tvAnnouncementTitle3.setText(annoucements.get(2).subject);
        tvAnnouncementComefrom3.setText(annoucements.get(2).gonggao);
        tvAnnouncementDate3.setText(annoucements.get(2).addat);
        List<Main.Data.News.Job> jobs = new ArrayList<>();
        jobs.addAll(main.data.news.jobs);
        tvJobsTitle1.setText(jobs.get(0).title);
        tvJobsDate1.setText(jobs.get(0).date);
        tvJobsTitle2.setText(jobs.get(1).title);
        tvJobsDate2.setText(jobs.get(1).date);
        tvJobsTitle3.setText(jobs.get(2).title);
        tvJobsDate3.setText(jobs.get(2).date);
        announcement1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailsActivity.actionStart(MainActivity.this, annoucements.get(0).index);
            }
        });
        announcement2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailsActivity.actionStart(MainActivity.this, annoucements.get(1).index);
            }
        });
        announcement3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailsActivity.actionStart(MainActivity.this, annoucements.get(2).index);
            }
        });
    }
}
