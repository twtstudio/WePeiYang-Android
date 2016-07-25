package com.twt.service.party.ui.submit.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.party.ui.BaseActivity;

import butterknife.InjectView;

/**
 * Created by dell on 2016/7/19.
 */
public class SubmitDetailActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.et_submit_title)
    EditText title;
    @InjectView(R.id.tv_submit_acc_name)
    TextView tvSubmitAccName;
    @InjectView(R.id.iv_submit_add_acc)
    ImageView ivSubmitAddAcc;
    @InjectView(R.id.et_submit_context)
    EditText context;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_submit_detail;
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
        return R.menu.menu_party_yes;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_submit_yes:
                if("".equals(title.getText().toString().trim()) || "".equals(context.getText().toString().trim())){
                    Toast.makeText(SubmitDetailActivity.this, "请输入标题和内容", Toast.LENGTH_SHORT).show();
                }
                else {

                }
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context,int type){
        Intent intent = new Intent(context, SubmitDetailActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }
}
