package com.twt.service.common.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.common.Presenter;

import butterknife.ButterKnife;

/**
 * Created by sunjuntao on 16/6/3.
 */
public abstract class PFragment<T extends Presenter> extends Fragment {
    protected T mPresenter;

    protected abstract T getPresenter();

    protected abstract int getLayout();

    protected abstract void initView();

    protected void preInitView() {

    }

    protected void afrerInitView() {
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.inject(this, view);
        preInitView();
        initView();
        afrerInitView();
        return view;
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
