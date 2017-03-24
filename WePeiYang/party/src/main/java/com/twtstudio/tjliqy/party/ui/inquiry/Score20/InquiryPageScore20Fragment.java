package com.twtstudio.tjliqy.party.ui.inquiry.Score20;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.bean.Score20Info;
import com.twtstudio.tjliqy.party.interactor.InquiryInteractorImpl;
import com.twtstudio.tjliqy.party.ui.BaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by dell on 2016/7/19.
 */
public class InquiryPageScore20Fragment extends BaseFragment implements Score20View{

    private List<Score20Info> list;

    private static final String ARG_PAGE = "ARG_PAGE";

    @BindView(R2.id.rv_inquiry)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_error_msg)
    TextView tvErrorMsg;
    private Score20Adapter adapter;

    private Score20Presenter presenter;

    public static InquiryPageScore20Fragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        InquiryPageScore20Fragment pageFragment = new InquiryPageScore20Fragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_party_inquiry_page_score20;
    }

    @Override
    public void preInitView() {
        adapter = new Score20Adapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initView() {
        presenter = new Score20PresenterImpl(this, new InquiryInteractorImpl());
        presenter.getScoreInfo();
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void create() {

    }

    @Override
    public void bindData(List<Score20Info> list) {
        recyclerView.setVisibility(View.VISIBLE);
        tvErrorMsg.setVisibility(View.GONE);
        adapter.addScore(list);
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        tvErrorMsg.setText(errorMsg);
    }
}