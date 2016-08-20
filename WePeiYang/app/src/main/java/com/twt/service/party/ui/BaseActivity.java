package com.twt.service.party.ui;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.twt.service.R;

import butterknife.ButterKnife;

/**
 * Created by dell on 2016/7/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract int getContentViewId();

    public abstract void preInitView();

    public abstract void initView();

    public abstract void afterInitView();

    public abstract Toolbar getToolbar();

    public abstract int getMenu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.inject(this);

        preInitView();
        initView();
        afterInitView();

        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.party_primary_color));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenu() != 0) {
            getMenuInflater().inflate(getMenu(),menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void setDialog(String msg,int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickPositiveButton(id);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickNegativeButton(id);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void onClickPositiveButton(int id){

    }

    public void onClickNegativeButton(int id){
    }
}
