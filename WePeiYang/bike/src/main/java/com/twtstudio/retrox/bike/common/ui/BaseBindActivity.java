package com.twtstudio.retrox.bike.common.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.common.ui.views.LoadingDialog;
import com.twtstudio.retrox.bike.utils.ToastUtils;


/**
 * Created by jcy on 16-10-23.
 *
 * @TwtStudio Mobile Develope Team
 */

public abstract class BaseBindActivity extends AppCompatActivity implements IViewController {

    protected LoadingDialog mLoadingDialog;


    protected void actionStart(Context context){

    };

    protected abstract int getStatusbarColor();

    protected void initView(@Nullable Bundle savedInstanceState){

    }


    protected void preInitView() {

    }

    protected void afterInitView() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionStart(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(getStatusbarColor()));
        }

        preInitView();
        initView(savedInstanceState);
        afterInitView();
    }

    protected void setUpToolbar(Toolbar toolbar){
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    @Override
    public void showLoadingDialog(String message) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void toastMessage(String message) {
        ToastUtils.showMessage(this,message);
    }
}
