package com.twt.service.party.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.party.ui.BaseActivity;

import butterknife.InjectView;

/**
 * Created by dell on 2016/7/19.
 */
public class SignActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.bt_sign_final_exam)
    Button btSignFinalExam;
    @InjectView(R.id.tv_sign_final_exam)
    TextView tvSignFinalExam;
    @InjectView(R.id.bt_sign_activists_school)
    Button btSignActivistsSchool;
    @InjectView(R.id.tv_sign_activists_school)
    TextView tvSignActivistsSchool;
    @InjectView(R.id.bt_sign_pro_school)
    Button btSignProSchool;
    @InjectView(R.id.bt_sign_change_class)
    Button btSignChangeClass;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_sign;
    }

    @Override
    public void preInitView() {

    }

    @Override
    public void initView() {

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

}
