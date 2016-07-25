package com.twt.service.party.ui.inquiry;

import android.os.Bundle;

import com.twt.service.R;
import com.twt.service.party.ui.BaseFragment;

/**
 * Created by dell on 2016/7/19.
 */
public class InquiryPage0Fragment extends BaseFragment {

    private static final String ARG_PAGE = "ARG_PAGE";

    public static InquiryPage0Fragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        InquiryPage0Fragment pageFragment = new InquiryPage0Fragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_party_inquiry_page1;
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

    }
}