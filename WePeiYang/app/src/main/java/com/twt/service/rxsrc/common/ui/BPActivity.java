package com.twt.service.rxsrc.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.twt.service.rxsrc.common.Presenter;


/**
 * Created by huangyong on 16/5/18.
 */
public abstract class BPActivity<T extends Presenter> extends BaseBindActivity {

    protected T mPresenter;

    protected abstract T getPresenter();

    @Override
    protected void afterInitView() {
        super.afterInitView();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = getPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}