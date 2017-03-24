package com.twtstudio.tjliqy.party.ui.inquiry.appeal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.interactor.InquiryInteractorImpl;
import com.twtstudio.tjliqy.party.ui.BaseActivity;
import com.twtstudio.tjliqy.party.ui.inquiry.other.OtherPresenter;
import com.twtstudio.tjliqy.party.ui.inquiry.other.OtherPresenterImpl;

import butterknife.BindView;

/**
 * Created by dell on 2016/7/19.
 */
public class AppealActivity extends BaseActivity implements ApealView {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.et_inquiry_title)
    EditText title;
    @BindView(R2.id.et_inquiry_context)
    EditText context;

    private OtherPresenter presenter;

    private Intent intent;

    private String type;

    private int testId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_inquiry_appeal;
    }

    @Override
    public void preInitView() {
        intent = getIntent();
        type = intent.getStringExtra("type");
        testId = intent.getIntExtra("test_id", 0);
        presenter = new OtherPresenterImpl(this, new InquiryInteractorImpl());
    }

    @Override
    public void initView() {

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle(R.string.appeal_party);
        return toolbar;
    }

    @Override
    public int getMenu() {
        return R.menu.menu_party_yes;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit_yes) {
            if ("".equals(title.getText().toString().trim()) || "".equals(context.getText().toString().trim())) {
                Toast.makeText(this, "请输入标题和内容", Toast.LENGTH_SHORT).show();
            } else {
                setDialog("确认要提交申诉吗？", 0);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickPositiveButton(int id) {
        presenter.appeal(title.getText().toString().trim(), context.getText().toString().trim(), type, testId);
    }

    public static void actionStart(Context context, String type, int testId) {
        Intent intent = new Intent(context, AppealActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("test_id", testId);
        context.startActivity(intent);
    }

    @Override
    public void appealFailure(String msg) {
        toastMsg(msg);
    }

    @Override
    public void appealSuccess(String msg) {
        toastMsg(msg);
        finish();
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(AppealActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
