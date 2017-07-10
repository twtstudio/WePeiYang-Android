package com.twt.service.home.common;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapadoo.alerter.Alerter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.view.RecyclerViewDivider;
import com.twtstudio.retrox.schedule.ScheduleActivity;
import com.twt.service.R;
import com.twt.service.base.BaseFragment;
import com.twt.service.databinding.FragmentCommonsBinding;
import com.twt.service.push.PushProvider;

/**
 * Created by retrox on 2016/12/12.
 */

public class CommonFragment extends BaseFragment {


    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private View mToolbar1;
    private View mToolbar2;

    private ImageView mZhangdan;
    private TextView mZhangdan_txt;
    private ImageView mTongxunlu;
    private ImageView mJiahao;
    private ImageView mZhangdan2;
    private ImageView mShaoyishao;
    private ImageView mSearch;
    private ImageView mZhaoxiang;
    private CommonFragViewModel commonFragViewModel;

    public static CommonFragment newInstance() {

        Bundle args = new Bundle();

        CommonFragment fragment = new CommonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentCommonsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_commons, container, false);
        commonFragViewModel = new CommonFragViewModel(this);
        binding.setViewModel(commonFragViewModel);

        View view = binding.getRoot();

//        initNewView(view);

        RecyclerViewDivider divider = new RecyclerViewDivider.Builder(this.getContext())
                .setSize(6f)
                .setColorRes(R.color.background_gray)
                .build();

        RecyclerView recyclerView = binding.recyclerView;

        recyclerView.addItemDecoration(divider);

        PushProvider pushProvider = new PushProvider((RxAppCompatActivity) this.getActivity());
        pushProvider.queryCourseMessage(coursePushBean -> {
            Alerter.create(this.getActivity())
                    .setTitle(coursePushBean.title)
                    .setText(coursePushBean.message)
                    .setDuration(3 * 1000)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CommonFragment.this.getActivity(), ScheduleActivity.class);
                            startActivity(intent);
                        }
                    })
//                    .setBackgroundColor(R.color.colorPrimaryLight)
                    .show();
        });


        return view;
    }

    public void refreshList() {
        commonFragViewModel.initList();
    }

    private void initNewView(View view) {
        /*
        collapsingToolbarLayout=(CollapsingToolbarLayout)view.findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.theme_color));

        mAppBarLayout=(AppBarLayout)view.findViewById(R.id.app_bar);
        mToolbar1=(View)view.findViewById(R.id.toolbar1);
        mToolbar2=(View)view.findViewById(R.id.toolbar2);

        mZhangdan=(ImageView)view.findViewById(R.id.img_zhangdan);
        mZhangdan_txt=(TextView)view.findViewById(R.id.img_zhangdan_txt);
        mTongxunlu=(ImageView)view.findViewById(R.id.tongxunlu);
        mJiahao=(ImageView)view.findViewById(R.id.jiahao);

        mZhangdan2=(ImageView)view.findViewById(R.id.img_shaomiao);
        mShaoyishao=(ImageView)view.findViewById(R.id.img_fukuang);
        mSearch=(ImageView)view.findViewById(R.id.img_search);
        mZhaoxiang=(ImageView)view.findViewById(R.id.img_zhaoxiang);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0){
                    //张开
                    mToolbar1.setVisibility(View.VISIBLE);
                    mToolbar2.setVisibility(View.GONE);
                    setToolbar1Alpha(255);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    //收缩
                    mToolbar1.setVisibility(View.GONE);
                    mToolbar2.setVisibility(View.VISIBLE);
                    setToolbar2Alpha(255);
                } else {
                    int alpha=255-Math.abs(verticalOffset);
                    if(alpha<0){
                        Log.e("alpha",alpha+"");
                        //收缩toolbar
                        mToolbar1.setVisibility(View.GONE);
                        mToolbar2.setVisibility(View.VISIBLE);
                        setToolbar2Alpha(Math.abs(verticalOffset));
                    }else{
                        //张开toolbar
                        mToolbar1.setVisibility(View.VISIBLE);
                        mToolbar2.setVisibility(View.GONE);
                        setToolbar1Alpha(alpha);
                    }
                }
            }
        });*/
    }

    //设置展开时各控件的透明度
    public void setToolbar1Alpha(int alpha) {
        mZhangdan.getDrawable().setAlpha(alpha);
        mZhangdan_txt.setTextColor(Color.argb(alpha, 255, 255, 255));
        mTongxunlu.getDrawable().setAlpha(alpha);
        mJiahao.getDrawable().setAlpha(alpha);
    }

    //设置闭合时各控件的透明度
    public void setToolbar2Alpha(int alpha) {
        mZhangdan2.getDrawable().setAlpha(alpha);
        mShaoyishao.getDrawable().setAlpha(alpha);
        mSearch.getDrawable().setAlpha(alpha);
        mZhaoxiang.getDrawable().setAlpha(alpha);
    }

}
