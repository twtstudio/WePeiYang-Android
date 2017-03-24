package com.twtstudio.tjliqy.party.ui.notification;

import android.os.Bundle;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.ui.BaseFragment;

/**
 * Created by dell on 2016/7/19.
 */
public class NotificationPageFragment extends BaseFragment {

    public static  final  String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_party_notification_page;
    }
    public static NotificationPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        NotificationPageFragment pageFragment = new NotificationPageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
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
