package com.example.caokun.fellowsearch.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.caokun.fellowsearch.R;
import com.example.caokun.fellowsearch.common.ui.BaseActivity;
import com.example.caokun.fellowsearch.common.ui.PActivity;
import com.example.caokun.fellowsearch.model.FellowController;
import com.example.caokun.fellowsearch.model.Institute;
import com.example.caokun.fellowsearch.model.Major;
import com.example.caokun.fellowsearch.model.Province;
import com.example.caokun.fellowsearch.model.RefreshEvent;
import com.example.caokun.fellowsearch.model.Senior;
import com.example.caokun.fellowsearch.model.Student;
import com.example.caokun.fellowsearch.presenter.FellowPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caokun on 2017/2/21.
 */

public class FellowFindActivity extends PActivity<FellowPresenter> implements FellowController {
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    TextView fellownum;
    Fellowadapter fellowadapter;
            TextView error;
    Button go_main;
    RelativeLayout relativeLayout;
    String province,institute,major,senior;
    @Override
    protected int getLayout(){
        return R.layout.activity_fellowcontent;
    }
    @Override
    protected void actionStart(Context context){
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mRecyclerView=(RecyclerView)findViewById(R.id.re_view);
        fellownum=(TextView)findViewById(R.id.fellownum);
        error=(TextView)findViewById(R.id.error);
        go_main=(Button)findViewById(R.id.go_main);
        relativeLayout=(RelativeLayout)findViewById(R.id.notfound);

    }
    @Override
    protected int getStatusbarColor() {
        return R.color.main_primary;
    }

    @Override
    protected Toolbar getToolbar() {
        mToolbar.setTitle("查询结果");
        return mToolbar;
    }

    @Override
    protected void preInitView(){
        fellowadapter=new Fellowadapter(this);
        onActionStart(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onEvent(RefreshEvent event){
        Log.d("event", "onEvent: ok.......");
    }

    @Override
    protected void initView() {
        mPresenter.getStudent(province,institute,major,senior);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(fellowadapter);

        province=null;
        institute=null;
        senior=null;
        major=null;
    }

    public  void onActionStart(Context context){
        Intent intent = getIntent();
        if(intent.getStringExtra("province")!=null) {
            province = intent.getStringExtra("province");
        }
        if(intent.getStringExtra("institute")!=null)
        {
            institute=intent.getStringExtra("institute");
            if(institute==null||institute.isEmpty()){
                institute="学院";
            }
        }
        else{
            institute="学院";
        }
        if(intent.getStringExtra("major")!=null) {
            major = intent.getStringExtra("major");
            if(major==null || major.isEmpty()){
                major="专业";
            }
        }
        else{
            major="专业";
        }
        if(intent.getStringExtra("senior")!=null) {
            senior = intent.getStringExtra("senior");
            if(senior==null||senior.isEmpty()){
                senior="高中";
            }
        }
        else{
            senior="高中";
        }
    }
    @Override
    protected FellowPresenter getPresenter() {
        return new FellowPresenter(this, this);
    }

    @Override
    public void bindStudentData(List<Student> students) {
        if(null==students){

            go_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setClass(FellowFindActivity.this, MainActivity.class);
//                    startActivity(intent);
                    relativeLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });
            relativeLayout.setVisibility(View.VISIBLE);
        }
        else {
            fellowadapter.refreshItems(students);
            fellownum.setText("为你找到" + fellowadapter.getItemCount() + "个小伙伴");
        }
    }




}
