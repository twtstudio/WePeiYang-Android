package com.twt.service.party.ui.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.twt.service.R;
import com.twt.service.party.ui.BaseActivity;
import com.twt.service.party.bean.Status;
import com.twt.service.party.interactor.PersonalStatusInteractorImpl;
import com.twt.service.party.ui.forum.ForumActivity;
import com.twt.service.party.ui.inquiry.InquiryActivity;
import com.twt.service.party.ui.notification.NotificationActivity;
import com.twt.service.party.ui.sign.SignActivity;
import com.twt.service.party.ui.study.StudyActivity;
import com.twt.service.party.ui.submit.SubmitActivity;
import com.twt.service.ui.bind.BindActivity;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.login.LoginActivity;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell on 2016/7/18.
 */
public class PartyActivity extends BaseActivity implements PartyView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.action_sign)
    FloatingActionButton actionSign;
    @InjectView(R.id.action_study)
    FloatingActionButton actionStudy;
    @InjectView(R.id.action_inquiry)
    FloatingActionButton actionInquiry;
    @InjectView(R.id.action_submit)
    FloatingActionButton actionSubmit;
    @InjectView(R.id.action_forum)
    FloatingActionButton actionForum;

    private PartyPresenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_home;
    }

    @Override
    public void preInitView() {
        presenter = new PartyPresenterImpl(this,new PersonalStatusInteractorImpl());
        presenter.loadPersonalStatus();
    }

    @Override
    public void initView() {

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle(R.string.party);
        return toolbar;
    }

    @Override
    public int getMenu() {
        return R.menu.menu_party_home;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PartyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void startBindActivity() {
        BindActivity.actionStart(this, NextActivity.Party);
        finish();
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.actionStart(this, NextActivity.Party);
        finish();
    }

    @OnClick({R.id.action_forum,R.id.action_inquiry,R.id.action_sign,R.id.action_study,R.id.action_submit})
    void onClick(View v){
        switch (v.getId()){
            case R.id.action_forum:
                ForumActivity.actionStart(this);
                break;
            case R.id.action_inquiry:
                InquiryActivity.actionStart(this);
                break;
            case R.id.action_submit:
                SubmitActivity.actionStart(this);
                break;
            case R.id.action_study:
                StudyActivity.actionStart(this);
                break;
            case R.id.action_sign:
                SignActivity.actionStart(this);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_notification:
                NotificationActivity.actionStart(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void toastMsg(String msg) {
        if (msg != null) {
            Toast.makeText(PartyActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void bindData(List<Status.StatusIdBean> ids) {
//        text.setText(ids.get(0).getMsg());
    }
}
