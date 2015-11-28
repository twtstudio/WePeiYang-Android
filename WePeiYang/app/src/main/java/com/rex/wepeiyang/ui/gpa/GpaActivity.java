package com.rex.wepeiyang.ui.gpa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.bean.GpaCaptcha;
import com.rex.wepeiyang.interactor.GpaInteractorImpl;
import com.rex.wepeiyang.ui.BaseActivity;
import com.rex.wepeiyang.ui.common.TabFragmentAdapter;
import com.rex.wepeiyang.ui.gpa.analysis.AnalysisFragment;
import com.rex.wepeiyang.ui.gpa.score.ScoreFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class GpaActivity extends BaseActivity implements GpaView {
    @InjectView(R.id.tbl_gpa)
    TabLayout tblGpa;
    @InjectView(R.id.vp_gpa)
    ViewPager vpGpa;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.pb_gpa)
    ProgressBar pbGpa;

    private ScoreFragment scoreFragment = new ScoreFragment();
    private AnalysisFragment analysisFragment = new AnalysisFragment();
    public static GpaPresenter presenter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GpaActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTab();
        presenter = new GpaPresenterImpl(this, new GpaInteractorImpl());
        presenter.getGpaWithoutToken();
    }


    @Override
    public void initTab() {
        List<String> tabList = new ArrayList<>();
        tabList.add("我的成绩单");
        tabList.add("成绩分析");
        tblGpa.addTab(tblGpa.newTab().setText(tabList.get(0)));
        tblGpa.addTab(tblGpa.newTab().setText(tabList.get(1)));
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(scoreFragment);
        fragmentList.add(analysisFragment);
        TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        vpGpa.setAdapter(fragmentAdapter);
        tblGpa.setupWithViewPager(vpGpa);
        tblGpa.setTabsFromPagerAdapter(fragmentAdapter);

    }

    @Override
    public void showProgress() {
        pbGpa.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbGpa.setVisibility(View.GONE);
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindData(Gpa gpa) {
        scoreFragment.bindData(gpa);
    }

    @Override
    public void showCaptchaDialog(GpaCaptcha gpaCaptcha) {
        CaptchaDialogFragment fragment = new CaptchaDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("token", gpaCaptcha.token);
        bundle.putString("raw", gpaCaptcha.raw);
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "Captcha Dialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                GpaActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
