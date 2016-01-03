package com.rex.wepeiyang.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.support.PrefUtils;
import com.rex.wepeiyang.ui.BaseActivity;
import com.rex.wepeiyang.ui.common.NextActivity;
import com.rex.wepeiyang.ui.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tv_account_name)
    TextView tvAccountName;
    @InjectView(R.id.btn_log_out)
    Button btnLogOut;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //tvAccountName.setText(PrefUtils.getTjuUname());
        tvAccountName.setText(PrefUtils.getUsername());
        btnLogOut.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log_out:
                PrefUtils.setLogin(false);
                PrefUtils.setUsername("");
                PrefUtils.setToken("");
                LoginActivity.actionStart(this, NextActivity.Main);
                finish();
        }
    }
}
