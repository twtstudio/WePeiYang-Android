package com.twt.service.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by huangyong on 16/5/18.
 */
public abstract class BaseFragment extends Fragment {

    protected abstract int getLayout();

    protected void preInitView() {

    }

    protected void afterInitView(){

    }

    protected abstract void initView();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.inject(this, view);
        preInitView();
        initView();
        afterInitView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
