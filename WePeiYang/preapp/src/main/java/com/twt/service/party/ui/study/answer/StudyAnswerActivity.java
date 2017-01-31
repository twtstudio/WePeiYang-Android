package com.twt.service.party.ui.study.answer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.twt.service.R;
import com.twt.service.party.bean.QuizInfo;
import com.twt.service.party.interactor.StudyInteractorImpl;
import com.twt.service.party.ui.BaseActivity;
import com.twt.service.party.ui.study.StudyPresenter;
import com.twt.service.party.ui.study.StudyPresenterImpl;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by tjliqy on 2016/8/23.
 */
public class StudyAnswerActivity extends BaseActivity implements StudyAnswerBridge, StudyAnswerView {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.vp_answer)
    ViewPager vpAnswer;

    @InjectView(R.id.tv_answer_submit)
    Button tvAnswerSubmit;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @InjectView(R.id.tv_answer_answered)
    TextView tvAnswerAnswered;
    @InjectView(R.id.tv_answer_all)
    TextView tvAnswerAll;
    @InjectView(R.id.rv_answer)
    RecyclerView rvAnswer;
    @InjectView(R.id.tv_loading)
    TextView tvLoading;


    private List<QuizInfo.DataBean> list;

    private ViewPagerAdapter adapter;

    private StudyPresenter presenter;

    private int courseId;

    private int[] exerciseAnswer;

    private int[] rightAnswer;

    private int answerNum = 0;

    private StudyAnswerRecyclerAdapter recyclerAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_answer;
    }

    @Override
    public void preInitView() {
        slidingLayout.setScrollableViewHelper(new NestedScrollableViewHelper());
        presenter = new StudyPresenterImpl(this, new StudyInteractorImpl());

        Intent intent = getIntent();
        courseId = intent.getIntExtra("id", 0);

        rvAnswer.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        recyclerAdapter = new StudyAnswerRecyclerAdapter(this, this);
        rvAnswer.setAdapter(recyclerAdapter);

    }

    @Override
    public void initView() {
        if (courseId != 0) {
            presenter.getQuiz(courseId);
        } else {
            toastMsg("数据错误");
        }
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle("20课答题");
        return toolbar;
    }

    @Override
    public boolean onMenuClickActions(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                if (rightAnswer != null && rightAnswer.length != vpAnswer.getCurrentItem()) {
                    vpAnswer.setCurrentItem(vpAnswer.getCurrentItem() + 1, true);
                } else {
                    toastMsg("没有下一题了哦~");
                }
        }
        return true;
    }

    @Override
    public int getMenu() {
        return R.menu.menu_party_answer;
    }


    @Override
    public void onBackPressed() {
        if (rightAnswer != null) {
            setDialog("退出后将丢失答题进度，确认退出吗？", 0);
        }
        else {
            super.onBackPressed();
        }
    }

    private boolean isFinishAll() {
        if (rightAnswer == null) {
            return false;
        }
        for (int i = 0; i < exerciseAnswer.length; i++) {
            if (exerciseAnswer[i] == 0) {
                return false;
            }
        }
        return true;
    }

    public static void actionStart(Context context, int courseId) {
        Intent intent = new Intent(context, StudyAnswerActivity.class);
        intent.putExtra("id", courseId);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_answer_submit)
    public void onClick() {
        if (isFinishAll()) {
            setDialog("已经完成全部题目，确认交卷吗？", 1);
        } else {
            Toast.makeText(StudyAnswerActivity.this, "还有题目没有作答，无法交卷", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickPositiveButton(int id) {
        switch (id) {
            case 0:
                finish();
                break;
            case 1:
                StudyResultActivity.actionStart(this,courseId,rightAnswer,exerciseAnswer);
                finish();
        }
    }

    @Override
    public void setAnswer(int position, int pos) {
        if (exerciseAnswer[position] == 0) {
            answerNum++;
            tvAnswerAnswered.setText(answerNum + "");
        }
        exerciseAnswer[position] += pos;
        recyclerAdapter.changeItemStatus(position, exerciseAnswer[position]);
    }

    @Override
    public void cancelAnswer(int position, int pos) {
        exerciseAnswer[position] -= pos;
        if (exerciseAnswer[position] == 0) {
            answerNum--;
            tvAnswerAnswered.setText(answerNum + "");
            recyclerAdapter.changeItemStatus(position, exerciseAnswer[position]);
        }
    }

    @Override
    public void setSingleAnswer(int position, int pos) {
        if (exerciseAnswer[position] == 0) {
            answerNum++;
            tvAnswerAnswered.setText(answerNum + "");
        }
        exerciseAnswer[position] = pos;
        recyclerAdapter.changeItemStatus(position, exerciseAnswer[position]);
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(StudyAnswerActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindData(List<QuizInfo.DataBean> dataList) {
        //用户答案数组初始化
        exerciseAnswer = new int[dataList.size()];
        rightAnswer = new int[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            exerciseAnswer[i] = 0;
            rightAnswer[i] = dataList.get(i).getExercise_answer();
        }
        tvAnswerAll.setText("/" + dataList.size());
        adapter = new ViewPagerAdapter(this, this, dataList);
        tvLoading.setVisibility(View.GONE);
        vpAnswer.setAdapter(adapter);
        vpAnswer.setOnPageChangeListener(adapter);
        recyclerAdapter.changeStatus(exerciseAnswer);
    }

    @Override
    public void click(int position) {
        vpAnswer.setCurrentItem(position, false);
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void setError(String msg) {
        tvLoading.setText(msg);
    }
}
