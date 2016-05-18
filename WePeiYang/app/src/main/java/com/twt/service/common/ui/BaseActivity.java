package com.twt.service.common.ui;

import android.support.v7.app.AppCompatActivity;

import com.twt.service.common.ui.views.LoadingDialog;

/**
 * Created by huangyong on 16/5/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected LoadingDialog mLoadingDialog;

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    public void showLoadingDialog(String message) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
