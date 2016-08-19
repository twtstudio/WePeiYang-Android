package com.twt.service.party.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.party.bean.TestInfo;
import com.twt.service.party.interactor.SignTestInteractorImpl;
import com.twt.service.party.ui.BaseActivity;

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

    private int academy_id;

    private int applicant_id;

    private int probationary_id;

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
        switch (type) {
            case SignPresenterImpl.TYPE_APPLICANT:
                if ("1".equals(testInfo.getTest_status())){
                    btSignApplicant.setBackgroundResource(R.color.myButtonColorRed);
                    btSignApplicant.setClickable(true);
                }else {
                    btSignAcademy.setBackgroundResource(R.color.myButtonColorGreen);
                    btSignAcademy.setText("已经报名");
                }
                applicant_id = testInfo.getTest_id();
                tvSignApplicant.setText(testInfo.getTest_name());
                break;
            case SignPresenterImpl.TYPE_ACADEMY:
                btSignAcademy.setBackgroundResource(R.color.myButtonColorRed);
                academy_id = testInfo.getTest_id();
                btSignAcademy.setClickable(true);
                tvSignAcademy.setText(testInfo.getTest_name());
                break;
            case SignPresenterImpl.TYPE_PROBATIONARY:
                // TODO: 2016/8/16 下拉菜单逻辑没有完成
                break;
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
                // TODO: 2016/8/16 下拉菜单的信息
                break;
        }
    }
    //TODO 得到返回的testinfo后按钮的处理未完成
    @OnClick({R.id.bt_sign_applicant, R.id.bt_sign_academy, R.id.bt_sign_probationary, R.id.bt_sign_change_class})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_sign_applicant:
                presenter.signTest(SignPresenterImpl.TYPE_APPLICANT,applicant_id);
                break;
            case R.id.bt_sign_academy:
                presenter.signTest(SignPresenterImpl.TYPE_ACADEMY,academy_id);
                break;
            case R.id.bt_sign_probationary:
                break;
            case R.id.bt_sign_change_class:
                break;
        }
    }
}
