package com.twt.service.rxsrc.common.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.rxsrc.common.Presenter;


/**
 * Created by sunjuntao on 16/6/3.
 */
public abstract class PFragment<T extends Presenter> extends BaseFragment {
    protected T mPresenter;

    protected abstract T getPresenter();

    @Override
    protected void afterInitView() {
        super.afterInitView();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = getPresenter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
