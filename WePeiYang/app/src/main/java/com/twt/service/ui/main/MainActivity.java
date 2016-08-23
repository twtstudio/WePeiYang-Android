package com.twt.service.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.squareup.picasso.Picasso;
import com.twt.service.JniUtils;
import com.twt.service.R;
import com.twt.service.bean.Login;
import com.twt.service.bean.Main;
import com.twt.service.bean.Update;
import com.twt.service.bike.bike.bikeAuth.BikeAuthActivity;
import com.twt.service.bike.bike.ui.main.BikeActivity;
import com.twt.service.interactor.MainInteractorImpl;
import com.twt.service.party.ui.home.PartyActivity;
import com.twt.service.party.ui.home.PartyView;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.about.AboutActivity;
import com.twt.service.ui.account.AccountActivity;
import com.twt.service.ui.bus.BusActivity;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.date.DatingActivity;
import com.twt.service.ui.feedback.FeedbackActivity;
import com.twt.service.ui.gpa.GpaActivity;
import com.twt.service.ui.jobs.JobsActivity;
import com.twt.service.ui.jobs.jobsdetails.JobsDetailsActivity;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.main.adapter.MainFoundAdapter;
import com.twt.service.ui.main.adapter.MainLostAdapter;
import com.twt.service.ui.news.NewsActivity;
import com.twt.service.ui.news.details.NewsDetailsActivity;
import com.twt.service.ui.notice.NoticeActivity;
import com.twt.service.ui.push.PushActivity;
import com.twt.service.ui.tools.ToolsActivity;
import com.twt.service.ui.schedule.ScheduleActivity;
import com.wooplr.spotlight.SpotlightView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

public class MainActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener, MainView {

    @InjectView(R.id.dl_main)
    DrawerLayout dlMain;
    @InjectView(R.id.btn_gpa_query)
    LinearLayout btnGpaQuery;
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
    @InjectView(R.id.tv_campusnews_title1)
    TextView tvCampusnewsTitle1;
    @InjectView(R.id.tv_campusnews_viewcount1)
    TextView tvCampusnewsViewcount1;
    @InjectView(R.id.tv_campusnews_date1)
    TextView tvCampusnewsDate1;
    @InjectView(R.id.tv_campusnews_title2)
    TextView tvCampusnewsTitle2;
    @InjectView(R.id.tv_campusnews_viewcount2)
    TextView tvCampusnewsViewcount2;
    @InjectView(R.id.tv_campusnews_date2)
    TextView tvCampusnewsDate2;
    @InjectView(R.id.tv_campusnews_title3)
    TextView tvCampusnewsTitle3;
    @InjectView(R.id.tv_campusnews_viewcount3)
    TextView tvCampusnewsViewcount3;
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
    @InjectView(R.id.btn_schedule)
    LinearLayout btnSchedule;
    @InjectView(R.id.btn_more)
    LinearLayout btnMore;
    @InjectView(R.id.iv_campus_picture1)
    ImageView ivCampusPicture1;
    @InjectView(R.id.campusnews1)
    LinearLayout campusnews1;
    @InjectView(R.id.iv_campus_picture2)
    ImageView ivCampusPicture2;
    @InjectView(R.id.iv_campus_picture3)
    ImageView ivCampusPicture3;
    @InjectView(R.id.drawer)
    NavigationView drawer;
    @InjectView(R.id.srl_main)
    SwipeRefreshLayout srlMain;
    @InjectView(R.id.rl_more_jobs_info)
    LinearLayout rlMoreJobsInfo;
    @InjectView(R.id.tv_jobs_title1)
    TextView tvJobsTitle1;
    @InjectView(R.id.tv_jobs_date1)
    TextView tvJobsDate1;
    @InjectView(R.id.jobs1)
    LinearLayout jobs1;
    @InjectView(R.id.tv_jobs_title2)
    TextView tvJobsTitle2;
    @InjectView(R.id.tv_jobs_date2)
    TextView tvJobsDate2;
    @InjectView(R.id.jobs2)
    LinearLayout jobs2;
    @InjectView(R.id.tv_jobs_title3)
    TextView tvJobsTitle3;
    @InjectView(R.id.tv_jobs_date3)
    TextView tvJobsDate3;
    @InjectView(R.id.jobs3)
    LinearLayout jobs3;
    @InjectView(R.id.rv_main_lost)
    RecyclerView rvMainLost;
    @InjectView(R.id.rv_main_found)
    RecyclerView rvMainFound;
    @InjectView(R.id.btn_bike)
    LinearLayout btnBike;

    private MainPresenterImpl presenter;
    private MainFoundAdapter mainFoundAdapter;
    private MainLostAdapter mainLostAdapter;

    private static final int BUTTONS = 6;//功能按钮的个数;
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
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
                    /*case R.id.push_settings:
                        PushActivity.actionStart(MainActivity.this);
                        break;*/
                }
                dlMain.closeDrawers();
                return true;
            }
        });

        //btnLibraryQuery.setOnClickListener(this);
        rlMoreCampusNews.setOnClickListener(this);
        rlMoreCampusNote.setOnClickListener(this);
        rlMoreJobsInfo.setOnClickListener(this);
        btnGpaQuery.setOnClickListener(this);
        btnSchedule.setOnClickListener(this);
        btnBike.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        presenter = new MainPresenterImpl(this, new MainInteractorImpl(), this);
        presenter.loadDataFromCache();
        srlMain.setColorSchemeColors(getResources().getColor(R.color.main_primary));
        srlMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadDataFromNet();
            }
        });
        checkUpdate(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_primary));
        }
        mainFoundAdapter = new MainFoundAdapter(this);
        mainLostAdapter = new MainLostAdapter(this);
        rvMainFound.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvMainLost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvMainFound.setAdapter(mainFoundAdapter);
        rvMainLost.setAdapter(mainLostAdapter);

//        //新手引导
//        new SpotlightView.Builder(this)
//                .introAnimationDuration(400)
//                .enableRevalAnimation(true)
//                .performClick(true)
//                .fadeinTextDuration(400)
//                .headingTvColor(Color.parseColor("#eb273f"))
//                .headingTvSize(32)
//                .headingTvText("BOOM！")
//                .subHeadingTvColor(Color.parseColor("#ffffff"))
//                .subHeadingTvSize(16)
//                .subHeadingTvText("工具栏移动到了这里哦~\n快来体验吧~")
//                .maskColor(Color.parseColor("#dc000000"))
//                .target(boomMenuButton)
//                .lineAnimDuration(400)
//                .lineAndArcColor(Color.parseColor("#eb273f"))
//                .dismissOnTouch(true)
//                .enableDismissAfterShown(true)
//                .show();

        //HrDialog 弹出
        if(PrefUtils.isShowDiaLog()){
            HrDialog hrDialog = new HrDialog();
            hrDialog.show(getFragmentManager(),"HrDialog");
        }

    }

    public void onEvent(SuccessEvent successEvent) {
        presenter.onSuccess(successEvent.toString());
    }

    public void onEvent(FailureEvent failureEvent) {
        presenter.onFailure(failureEvent.getRetrofitError());
    }

    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
//            case R.id.btn_bus:
//                if (PrefUtils.isLogin()) {
//                    BusActivity.actionStart(this);
//                } else {
//                    LoginActivity.actionStart(this, NextActivity.Bus);
//                }
//                break;
//            case R.id.btn_dating:
//                if (PrefUtils.isLogin()) {
//                    DatingActivity.actionStart(this);
//                } else {
//                    LoginActivity.actionStart(this, NextActivity.Dating);
//                }
//                break;
//            case R.id.btn_party:
//                if (PrefUtils.isLogin()) {
//                    PartyActivity.actionStart(this);
//                } else {
//                    LoginActivity.actionStart(this, NextActivity.Party);
//                }
//                break;
            case R.id.btn_bike:
                if (PrefUtils.isLogin()){
                    BikeActivity.actionStart(this);
                }else {
                    LoginActivity.actionStart(this,NextActivity.Bike);
                }break;
            case R.id.btn_more:
                ToolsActivity.actionStart(this);
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
            case R.id.rl_more_jobs_info:
                JobsActivity.actionStart(this);
                break;
        }
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindData(final Main main) {
        View drawerHeader = drawer.getHeaderView(0);
        TextView tvUsername = (TextView) drawerHeader.findViewById(R.id.tv_username);
        tvUsername.setText(PrefUtils.getUsername());
        TextSliderView banner1 = new TextSliderView(this);
        banner1.description(main.data.carousel.get(0).subject + "\n").image(main.data.carousel.get(0).pic);
        TextSliderView banner2 = new TextSliderView(this);
        banner2.description(main.data.carousel.get(1).subject + "\n").image(main.data.carousel.get(1).pic);
        TextSliderView banner3 = new TextSliderView(this);
        banner3.description(main.data.carousel.get(2).subject + "\n").image(main.data.carousel.get(2).pic);
        TextSliderView banner4 = new TextSliderView(this);
        banner4.description(main.data.carousel.get(3).subject + "\n").image(main.data.carousel.get(3).pic);
        TextSliderView banner5 = new TextSliderView(this);
        banner5.description(main.data.carousel.get(4).subject + "\n").image(main.data.carousel.get(4).pic);
        slider.removeAllSliders();
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
        final List<Main.Data.News.Job> jobs = new ArrayList<>();
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
        jobs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobsDetailsActivity.actionStart(MainActivity.this, jobs.get(0).id);
            }
        });
        jobs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobsDetailsActivity.actionStart(MainActivity.this, jobs.get(1).id);
            }
        });
        jobs3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobsDetailsActivity.actionStart(MainActivity.this, jobs.get(2).id);
            }
        });
        mainFoundAdapter.bindData(main.data.service.found);
        mainLostAdapter.bindData(main.data.service.lost);
    }

    @Override
    public void hideRefreshing() {
        srlMain.setRefreshing(false);
    }


    private void checkUpdate(final boolean isInMain) {
        JniUtils jniUtils = JniUtils.getInstance();
        FIR.checkForUpdateInFIR(jniUtils.getFirApiToken(), new VersionCheckCallback() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
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
