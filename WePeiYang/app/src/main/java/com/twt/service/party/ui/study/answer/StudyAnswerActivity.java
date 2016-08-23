package com.twt.service.party.ui.study.answer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.twt.service.R;
import com.twt.service.party.bean.QuizInfo;
import com.twt.service.party.ui.BaseActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by tjliqy on 2016/8/23.
 */
public class StudyAnswerActivity extends BaseActivity implements ViewPager.OnPageChangeListener{


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.vp_answer)
    ViewPager vpAnswer;
    @InjectView(R.id.tv_answer_number)
    TextView tvAnswerNumber;
    @InjectView(R.id.tv_answer_submit)
    Button tvAnswerSubmit;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;


    private List<QuizInfo.DataBean> list;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_answer;
    }

    @Override
    public void preInitView() {
        // TODO: 2016/8/23 需要调用nest...Helper的方法。。大概
        slidingLayout.setScrollableViewHelper(new NestedScrollableViewHelper());
        ViewGroup.LayoutParams viewPagerImageViewParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
    }

    @Override
    public void initView() {

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public int getMenu() {
        return 0;
    }

    public static void actionStart(Context context, int course_id) {
        Intent intent = new Intent(context, StudyAnswerActivity.class);
        intent.putExtra("id", course_id);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_answer_submit)
    public void onClick() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int pageIndex = position;
        if(position == 0){

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
