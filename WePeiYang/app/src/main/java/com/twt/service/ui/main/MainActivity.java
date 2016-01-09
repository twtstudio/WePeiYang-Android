package com.twt.service.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.twt.service.JniUtils;
import com.twt.service.R;
import com.twt.service.bean.Main;
import com.twt.service.bean.Update;
import com.twt.service.interactor.MainInteractorImpl;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.about.AboutActivity;
import com.twt.service.ui.account.AccountActivity;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.feedback.FeedbackActivity;
import com.twt.service.ui.gpa.GpaActivity;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.news.NewsActivity;
import com.twt.service.ui.news.details.NewsDetailsActivity;
import com.twt.service.ui.notice.NoticeActivity;
import com.twt.service.ui.schedule.ScheduleActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

public class MainActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener, MainView {

    @InjectView(R.id.dl_main)
    DrawerLayout dlMain;
    @InjectView(R.id.btn_gpa_query)
    LinearLayout btnGpaQuery;
    //@InjectView(R.id.btn_library_query)
    //LinearLayout btnLibraryQuery;
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
    //@InjectView(R.id.rl_more_jobs_info)
    //LinearLayout rlMoreJobsInfo;
    @InjectView(R.id.tv_campusnews_title1)
    TextView tvCampusnewsTitle1;
    @InjectView(R.id.tv_campusnews_viewcount1)
    TextView tvCampusnewsViewcount1;
    //@InjectView(R.id.tv_campusnews_comment1)
    //TextView tvCampusnewsComment1;
    @InjectView(R.id.tv_campusnews_date1)
    TextView tvCampusnewsDate1;
    @InjectView(R.id.tv_campusnews_title2)
    TextView tvCampusnewsTitle2;
    @InjectView(R.id.tv_campusnews_viewcount2)
    TextView tvCampusnewsViewcount2;
    //@InjectView(R.id.tv_campusnews_comment2)
    //TextView tvCampusnewsComment2;
    @InjectView(R.id.tv_campusnews_date2)
    TextView tvCampusnewsDate2;
    @InjectView(R.id.tv_campusnews_title3)
    TextView tvCampusnewsTitle3;
    @InjectView(R.id.tv_campusnews_viewcount3)
    TextView tvCampusnewsViewcount3;
    //@InjectView(R.id.tv_campusnews_comment3)
    //TextView tvCampusnewsComment3;
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
    //@InjectView(R.id.tv_jobs_title1)
    //TextView tvJobsTitle1;
    //@InjectView(R.id.tv_jobs_date1)
    //TextView tvJobsDate1;
    //@InjectView(R.id.tv_jobs_title2)
    //TextView tvJobsTitle2;
    //@InjectView(R.id.tv_jobs_date2)
    //TextView tvJobsDate2;
    //@InjectView(R.id.tv_jobs_title3)
    //TextView tvJobsTitle3;
    //@InjectView(R.id.tv_jobs_date3)
    //TextView tvJobsDate3;
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
    //@InjectView(R.id.jobs1)
    //LinearLayout jobs1;
    //@InjectView(R.id.jobs2)
    //LinearLayout jobs2;
    //@InjectView(R.id.jobs3)
    //LinearLayout jobs3;
    @InjectView(R.id.btn_schedule)
    LinearLayout btnSchedule;
    @InjectView(R.id.iv_campus_picture1)
    ImageView ivCampusPicture1;
    @InjectView(R.id.campusnews1)
    LinearLayout campusnews1;
    @InjectView(R.id.iv_campus_picture2)
    ImageView ivCampusPicture2;
    @InjectView(R.id.iv_campus_picture3)
    ImageView ivCampusPicture3;
    @InjectView(R.id.pb_main)
    ProgressBar pbMain;
    @InjectView(R.id.drawer)
    NavigationView drawer;

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
                        if (PrefUtils.isLogin()) {
                            AccountActivity.actionStart(MainActivity.this);
                        } else {
                            LoginActivity.actionStart(MainActivity.this, NextActivity.Main);
                        }
                        break;
                    case R.id.item_give_advice:
                        FeedbackActivity.actionStart(MainActivity.this);
                        break;
                    case R.id.item_about:
                        AboutActivity.actionStart(MainActivity.this);
                        break;
                    /*case R.id.item_share_app:
                        break;*/
                    case R.id.item_check_update:
                        checkUpdate(false);
                        break;
                }
                dlMain.closeDrawers();
                return true;
            }
        });
        btnGpaQuery.setOnClickListener(this);
        //btnLibraryQuery.setOnClickListener(this);
        rlMoreCampusNews.setOnClickListener(this);
        rlMoreCampusNote.setOnClickListener(this);
        btnSchedule.setOnClickListener(this);
        presenter = new MainPresenterImpl(this, new MainInteractorImpl());
        presenter.loadData();
        checkUpdate(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_primary));
        }
    }

    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
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
            case R.id.btn_schedule:
                if (PrefUtils.isLogin()) {
                    ScheduleActivity.actionStart(this);
                } else {
                    LoginActivity.actionStart(this, NextActivity.Schedule);
                }
                break;
            case R.id.btn_gpa_query:
                if (PrefUtils.isLogin()) {
                    GpaActivity.actionStart(this);
                } else {
                    LoginActivity.actionStart(this, NextActivity.Gpa);
                }
                break;
            //case R.id.btn_library_query:
            //LibraryActivity.actionStart(this);
            //break;
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
    public void bindData(final Main main) {
        View drawerHeader = drawer.getHeaderView(0);
        TextView tvUsername = (TextView) drawerHeader.findViewById(R.id.tv_username);
        tvUsername.setText(PrefUtils.getUsername());
        TextSliderView banner1 = new TextSliderView(this);
        banner1.description(main.data.carousel.get(0).subject).image(main.data.carousel.get(0).pic);
        TextSliderView banner2 = new TextSliderView(this);
        banner2.description(main.data.carousel.get(1).subject).image(main.data.carousel.get(1).pic);
        TextSliderView banner3 = new TextSliderView(this);
        banner3.description(main.data.carousel.get(2).subject).image(main.data.carousel.get(2).pic);
        TextSliderView banner4 = new TextSliderView(this);
        banner4.description(main.data.carousel.get(3).subject).image(main.data.carousel.get(3).pic);
        TextSliderView banner5 = new TextSliderView(this);
        banner5.description(main.data.carousel.get(4).subject).image(main.data.carousel.get(4).pic);
        slider.addSlider(banner1);
        slider.addSlider(banner2);
        slider.addSlider(banner3);
        slider.addSlider(banner4);
        slider.addSlider(banner5);
        slider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        banner1.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                NewsDetailsActivity.actionStart(MainActivity.this, main.data.carousel.get(0).index);
            }
        });
        banner2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                NewsDetailsActivity.actionStart(MainActivity.this, main.data.carousel.get(1).index);
            }
        });
        banner3.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                NewsDetailsActivity.actionStart(MainActivity.this, main.data.carousel.get(2).index);
            }
        });
        banner4.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                NewsDetailsActivity.actionStart(MainActivity.this, main.data.carousel.get(3).index);
            }
        });
        banner5.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                NewsDetailsActivity.actionStart(MainActivity.this, main.data.carousel.get(4).index);
            }
        });
        final List<Main.Data.News.Campus> campuses = new ArrayList<>();
        campuses.addAll(main.data.news.campus);
        tvCampusnewsTitle1.setText(main.data.news.campus.get(0).subject);
        tvCampusnewsTitle2.setText(main.data.news.campus.get(1).subject);
        tvCampusnewsTitle3.setText(main.data.news.campus.get(2).subject);
        tvCampusnewsDate1.setText(campuses.get(0).addat);
        tvCampusnewsViewcount1.setText(campuses.get(0).visitcount);
        if (!campuses.get(0).pic.isEmpty()) {
            Picasso.with(this).load(campuses.get(0).pic).into(ivCampusPicture1);
        } else {
            ivCampusPicture1.setImageResource(R.mipmap.ic_login);
        }
        tvCampusnewsDate2.setText(campuses.get(1).addat);
        tvCampusnewsViewcount2.setText(campuses.get(1).visitcount);
        if (!campuses.get(1).pic.isEmpty()) {
            Picasso.with(this).load(campuses.get(1).pic).into(ivCampusPicture2);
        } else {
            ivCampusPicture2.setImageResource(R.mipmap.ic_login);
        }
        tvCampusnewsDate3.setText(campuses.get(2).addat);
        tvCampusnewsViewcount3.setText(campuses.get(2).visitcount);
        if (!campuses.get(2).pic.isEmpty()) {
            Picasso.with(this).load(campuses.get(2).pic).into(ivCampusPicture3);
        } else {
            ivCampusPicture3.setImageResource(R.mipmap.ic_login);
        }
        campusnews1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailsActivity.actionStart(MainActivity.this, campuses.get(0).index);
            }
        });
        campusnews2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailsActivity.actionStart(MainActivity.this, campuses.get(1).index);
            }
        });
        campusnews3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailsActivity.actionStart(MainActivity.this, campuses.get(2).index);
            }
        });
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
        //List<Main.Data.News.Job> jobs = new ArrayList<>();
        //jobs.addAll(main.data.news.jobs);
        //tvJobsTitle1.setText(jobs.get(0).title);
        //tvJobsDate1.setText(jobs.get(0).date);
        //tvJobsTitle2.setText(jobs.get(1).title);
        //tvJobsDate2.setText(jobs.get(1).date);
        //tvJobsTitle3.setText(jobs.get(2).title);
        //tvJobsDate3.setText(jobs.get(2).date);
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

    private void checkUpdate(final boolean isInMain) {
        JniUtils jniUtils = new JniUtils();
        FIR.checkForUpdateInFIR(jniUtils.getFirApiToken(), new VersionCheckCallback() {
            @Override
            public void onSuccess(String s) {
                Update update = new Gson().fromJson(s, Update.class);
                PackageInfo packageInfo = null;
                try {
                    packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = packageInfo.versionName;
                    if (!version.equals(update.versionShort)) {
                        UpdateDialogFragment dialogFragment = new UpdateDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("update", update);
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getFragmentManager(), "Update Dialog");
                    } else {
                        if (!isInMain) {
                            toastMessage("当前为最新版本");
                        }
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                super.onSuccess(s);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFail(Exception e) {
                Log.e("update", e.getMessage());
                super.onFail(e);
            }
        });
    }
}
