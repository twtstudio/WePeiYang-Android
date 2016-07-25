package com.twt.service.party.ui.submit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.twt.service.R;
import com.twt.service.party.ui.BaseActivity;
import com.twt.service.party.ui.submit.detail.SubmitDetailActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell on 2016/7/18.
 */
public class SubmitActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.bt_submit_to_application)
    Button btSubmitToApplication;
    @InjectView(R.id.bt_submit_to_report)
    Button btSubmitToReport;
    @InjectView(R.id.bt_submit_to_summary)
    Button btSubmitToSummary;
    @InjectView(R.id.bt_submit_to_positive)
    Button btSubmitToPositive;


    private static final int TYPE_APPLICATION = 1;
    private static final int TYPE_POSITIVE = 1;
    private static final int TYPE_REPORT = 1;
    private static final int TYPE_SUMMARY = 1;
    @Override
    public int getContentViewId() {
        return R.layout.activity_party_submit;
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
        toolbar.setTitle(R.string.submit_party);
        return toolbar;
    }

    @Override
    public int getMenu() {
        return 0;
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,SubmitActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.bt_submit_to_application,R.id.bt_submit_to_report,R.id.bt_submit_to_summary,R.id.bt_submit_to_positive})
    void onClick(View view){
        switch (view.getId()){
            case R.id.bt_submit_to_application:
                SubmitDetailActivity.actionStart(this,TYPE_APPLICATION);
                break;
            case R.id.bt_submit_to_positive:
                SubmitDetailActivity.actionStart(this,TYPE_POSITIVE);
                break;
            case R.id.bt_submit_to_report:
                SubmitDetailActivity.actionStart(this,TYPE_REPORT);
                break;
            case R.id.bt_submit_to_summary:
                SubmitDetailActivity.actionStart(this,TYPE_SUMMARY);
                break;
            default:
                break;
        }
    }
}
