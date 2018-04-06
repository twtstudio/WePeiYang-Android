package com.twtstudio.tjliqy.party.ui.study.answer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.interactor.StudyInteractorImpl;
import com.twtstudio.tjliqy.party.ui.BaseActivity;
import com.twtstudio.tjliqy.party.ui.study.StudyPresenter;
import com.twtstudio.tjliqy.party.ui.study.StudyPresenterImpl;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tjliqy on 2016/8/26.
 */
public class StudyResultActivity extends BaseActivity implements StudyResultView {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.iv_monkey)
    ImageView ivMonkey;
    @BindView(R2.id.bt_score)
    Button btScore;
    @BindView(R2.id.ll_finish)
    LinearLayout llFinish;
    @BindView(R2.id.tv_msg)
    TextView tvMsg;

    private int courseId;
    private int[] rightAnswer;
    private int[] exerciseAnswer;

    private StudyPresenter presenter;

    private boolean isSubmitFailure = true;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_study_result;
    }

    @Override
    public void preInitView() {
        Intent intent = getIntent();
        courseId = intent.getIntExtra("course_id", 0);
        rightAnswer = intent.getIntArrayExtra("right_answer");
        exerciseAnswer = intent.getIntArrayExtra("exercise_answer");
        presenter = new StudyPresenterImpl(this, new StudyInteractorImpl());
    }

    @Override
    public void initView() {
        presenter.submitAnswer(courseId, rightAnswer, exerciseAnswer);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle("考试结果");
        return toolbar;
    }

    @Override
    public int getMenu() {
        return 0;
    }

    public static void actionStart(Context context, int courseId, int[] rightAnswer, int[] exerciseAnswer) {
        Intent intent = new Intent(context, StudyResultActivity.class);
        intent.putExtra("course_id", courseId);
        intent.putExtra("right_answer", rightAnswer);
        intent.putExtra("exercise_answer", exerciseAnswer);
        context.startActivity(intent);
    }

    @OnClick({R2.id.bt_score, R2.id.ll_finish})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_score) {
            if (isSubmitFailure) {
                btScore.setText("正在交卷");
                presenter.submitAnswer(courseId, rightAnswer, exerciseAnswer);
            }
        }else if (id == R.id.ll_finish) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (isSubmitFailure) {
            setDialog("尚未完成交卷，确认退出吗？", 0);
        } else {
            finish();
        }
    }

    @Override
    public void onClickPositiveButton(int id) {
        finish();
    }

    @Override
    public void bindData(int status, int score, String msg) {
        if (status == 1) {//成绩没通过
            ivMonkey.setImageResource(R.mipmap.ic_party_cry);
        } else {//成绩通过
            ivMonkey.setImageResource(R.mipmap.ic_party_smail);
        }
        tvMsg.setText(msg);
        btScore.setText("考试成绩：" + score);
        isSubmitFailure = false;
    }

    @Override
    public void setErrorMsg(String msg) {
        btScore.setText("重新提交");
        ivMonkey.setImageResource(R.mipmap.ic_party_cry);
        isSubmitFailure = true;
        tvMsg.setText(msg);
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(StudyResultActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
