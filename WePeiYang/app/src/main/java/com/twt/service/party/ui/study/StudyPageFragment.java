package com.twt.service.party.ui.study;

import android.os.Bundle;

import com.twt.service.R;
import com.twt.service.party.ui.BaseFragment;

/**
 * Created by dell on 2016/7/19.
 */
public class StudyPageFragment extends BaseFragment {

    public static  final  String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static StudyPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        StudyPageFragment pageFragment = new StudyPageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_party_study_page;
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
    public void create() {
        mPage = getArguments().getInt(ARG_PAGE);
    }
}