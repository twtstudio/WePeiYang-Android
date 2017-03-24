package com.twtstudio.tjliqy.party.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.bean.TestInfo;
import com.twtstudio.tjliqy.party.interactor.SignTestInteractorImpl;
import com.twtstudio.tjliqy.party.ui.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2016/7/19.
 */
public class SignActivity extends BaseActivity implements SignView {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.bt_sign_applicant)
    Button btSignApplicant;
    @BindView(R2.id.tv_sign_applicant)
    TextView tvSignApplicant;
    @BindView(R2.id.bt_sign_academy)
    Button btSignAcademy;
    @BindView(R2.id.tv_sign_academy)
    TextView tvSignAcademy;
    @BindView(R2.id.bt_sign_probationary)
    Button btSignProbationary;
    @BindView(R2.id.tv_sign_probationary)
    TextView tvSignProbationary;
//    @BindView(R.id.bt_sign_change_class)
//    Button btSignChangeClass;

    private SignPresenter presenter;

    private int academy_id;

    private int applicant_id;

    private int probationary_id;

    private boolean b_academy = false;
    private boolean b_applicant = false;
    private boolean b_probationary = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_sign;
    }

    @Override
    public void preInitView() {

    }

    @Override
    public void initView() {
        presenter = new SignPresenterImpl(this, new SignTestInteractorImpl());
        presenter.getTest();
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle(R.string.sign_party);
        return toolbar;
    }

    @Override
    public int getMenu() {
        return 0;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SignActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void bindData(TestInfo testInfo, String type) {
        if (testInfo.getHas_entry() == 0) {
            switch (type) {
                case SignPresenterImpl.TYPE_APPLICANT:
                    b_applicant = true;
                    btSignApplicant.setBackgroundResource(R.drawable.shape_button_red);
                    applicant_id = testInfo.getTest_id();
                    tvSignApplicant.setText(testInfo.getTest_name() + "\n考试时间：" + testInfo.getTest_begintime());
                    break;
                case SignPresenterImpl.TYPE_ACADEMY:
                    b_academy = true;
                    btSignAcademy.setBackgroundResource(R.drawable.shape_button_red);
                    academy_id = testInfo.getTest_id();
                    tvSignAcademy.setText(testInfo.getTest_name() + "\n考试时间：" + testInfo.getTest_begintime());
                    break;
                case SignPresenterImpl.TYPE_PROBATIONARY:
                    b_probationary = true;
                    btSignProbationary.setBackgroundResource(R.drawable.shape_button_red);
                    probationary_id = testInfo.getTrain_id();
                    tvSignProbationary.setText(testInfo.getTrain_name() + "\n考试时间：" + testInfo.getTrain_begintime());
                    break;
            }
        } else {
            switch (type) {
                case SignPresenterImpl.TYPE_APPLICANT:
                    b_applicant = false;
                    btSignApplicant.setBackgroundResource(R.drawable.shape_button_green);
                    btSignApplicant.setText("已报名");
                    tvSignApplicant.setText(testInfo.getTest_name() + "\n考试时间：" + testInfo.getTest_begintime());
                    break;
                case SignPresenterImpl.TYPE_ACADEMY:
                    b_academy = false;
                    btSignAcademy.setBackgroundResource(R.drawable.shape_button_green);
                    btSignAcademy.setText("已报名");
                    tvSignAcademy.setText(testInfo.getTest_name() + "\n考试时间：" + testInfo.getTest_begintime());
                    break;
                case SignPresenterImpl.TYPE_PROBATIONARY:
                    b_probationary = false;
                    btSignProbationary.setBackgroundResource(R.drawable.shape_button_green);
                    btSignProbationary.setText("已报名");
                    tvSignProbationary.setText(testInfo.getTrain_name() + "\n考试时间：" + testInfo.getTrain_begintime());
                    break;
            }
        }
    }

    @Override
    public void setNoTestReason(String msg, String type) {
        switch (type) {
            case SignPresenterImpl.TYPE_APPLICANT:
                tvSignApplicant.setText(msg);
                break;
            case SignPresenterImpl.TYPE_ACADEMY:
                tvSignAcademy.setText(msg);
                break;
            case SignPresenterImpl.TYPE_PROBATIONARY:
                tvSignProbationary.setText(msg);
                break;
        }
    }

    @OnClick({R2.id.bt_sign_applicant, R2.id.bt_sign_academy, R2.id.bt_sign_probationary})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_sign_applicant) {
            if (b_applicant) {
                setDialog("是否要报名结业考试？", view.getId());
            } else {
                toastMsg("无法报名");
            }
        } else if (id == R.id.bt_sign_academy) {
            if (b_academy) {
                setDialog("是否要报名院级积极分子党校考试？", view.getId());
            } else {
                toastMsg("无法报名");
            }
        } else if (id == R.id.bt_sign_probationary) {
            if (b_probationary) {
                setDialog("是否要报名预备党员党校？", view.getId());
            } else {
                toastMsg("无法报名");
            }
        }
//            case R.id.bt_sign_change_class:
//                break;
    }

    @Override
    public void onClickPositiveButton(int id) {
        if (id == R.id.bt_sign_applicant) {
            presenter.signTest(SignPresenterImpl.TYPE_APPLICANT, applicant_id);
        } else if (id == R.id.bt_sign_academy) {
            presenter.signTest(SignPresenterImpl.TYPE_ACADEMY, academy_id);
        } else if (id == R.id.bt_sign_probationary) {
            presenter.signTest(SignPresenterImpl.TYPE_PROBATIONARY, probationary_id);
        }

    }
}
