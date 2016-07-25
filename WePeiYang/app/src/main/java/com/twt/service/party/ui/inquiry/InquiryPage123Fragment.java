package com.twt.service.party.ui.inquiry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.party.ui.BaseFragment;
import com.twt.service.party.ui.inquiry.appeal.AppealActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell on 2016/7/19.
 */
public class InquiryPage123Fragment extends BaseFragment {

    private static final String ARG_PAGE = "ARG_PAGE";
    @InjectView(R.id.bt_inquiry_to_appeal)
    Button btInquiryToAppeal;

    public static InquiryPage123Fragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        InquiryPage123Fragment pageFragment = new InquiryPage123Fragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_party_inquiry_page2;
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

    @OnClick(R.id.bt_inquiry_to_appeal)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_inquiry_to_appeal:
                Toast.makeText(getActivity(), "233", Toast.LENGTH_SHORT).show();
                AppealActivity.actionStart(getActivity(),(int)this.getArguments().get(ARG_PAGE));
        }
    }
}
