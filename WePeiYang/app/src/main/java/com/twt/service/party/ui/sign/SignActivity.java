package com.twt.service.party.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.party.bean.TestInfo;
import com.twt.service.party.interactor.SignTestInteractorImpl;
import com.twt.service.party.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell on 2016/7/19.
 */
public class SignActivity extends BaseActivity implements SignView {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.bt_sign_applicant)
    Button btSignApplicant;
    @InjectView(R.id.tv_sign_applicant)
    TextView tvSignApplicant;
    @InjectView(R.id.bt_sign_academy)
    Button btSignAcademy;
    @InjectView(R.id.tv_sign_academy)
    TextView tvSignAcademy;
    @InjectView(R.id.bt_sign_probationary)
    Button btSignProbationary;
    @InjectView(R.id.bt_sign_change_class)
    Button btSignChangeClass;

    private SignPresenter presenter;

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

    //TODO 得到返回的testinfo后相关的处理未完成
    @Override
    public void bindData(TestInfo testInfo, String type) {
        switch (type) {
            case SignPresenterImpl.TYPE_APPLICANT:
                break;
            case SignPresenterImpl.TYPE_ACADEMY:
                break;
            case SignPresenterImpl.TYPE_PROBATIONARY:
                break;
        }
    }
    //TODO 得到返回的testinfo无法报名的信息后相关的处理未完成
    @Override
    public void setNoTestReason(String msg, String type) {
        switch (type) {
            case SignPresenterImpl.TYPE_APPLICANT:
                break;
            case SignPresenterImpl.TYPE_ACADEMY:
                break;
            case SignPresenterImpl.TYPE_PROBATIONARY:
                break;
        }
    }
    //TODO 得到返回的testinfo后按钮的处理未完成
    @OnClick({R.id.bt_sign_applicant, R.id.bt_sign_academy, R.id.bt_sign_probationary, R.id.bt_sign_change_class})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_sign_applicant:
                break;
            case R.id.bt_sign_academy:
                break;
            case R.id.bt_sign_probationary:
                break;
            case R.id.bt_sign_change_class:
                break;
        }
    }
}
