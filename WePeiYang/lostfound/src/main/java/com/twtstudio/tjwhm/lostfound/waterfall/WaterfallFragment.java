package com.twtstudio.tjwhm.lostfound.waterfall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twtstudio.tjwhm.lostfound.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/3.
 **/

public class WaterfallFragment extends Fragment implements WaterfallContract.WaterfallView {
    @BindView(R.id.waterfall_refresh)
    SwipeRefreshLayout water_refresh;
    @BindView(R.id.waterfall_recyclerView)
    RecyclerView waterfall_recyclerView;
    private WaterfallTableAdapter tableAdapter;
    private StaggeredGridLayoutManager layoutManager;
    private WaterfallBean waterfallBean = new WaterfallBean();
    WaterfallContract.WaterfallPresenter waterfallPresenter = new WaterfallPresenterImpl(this);


    public static WaterfallFragment newInstance(String type) {

        Bundle args = new Bundle();
        args.putString("index", type);
        WaterfallFragment fragment = new WaterfallFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waterfall, container, false);
        ButterKnife.bind(this, view);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        waterfall_recyclerView.setLayoutManager(layoutManager);
        tableAdapter = new WaterfallTableAdapter(waterfallBean, getActivity());
        waterfallBean.data = new ArrayList<>();
        waterfall_recyclerView.setAdapter(tableAdapter);

        Bundle bundle = getArguments();
        final String lostOrFound = bundle.getString("index");
        waterfallPresenter.loadWaterfallData(lostOrFound, 1);

        water_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                waterfallBean.data.clear();
                waterfallPresenter.loadWaterfallData(lostOrFound, 1);
            }
        });
        return view;
    }

    @Override
    public void setWaterfallData(WaterfallBean waterfallBean) {
        this.waterfallBean.error_code = waterfallBean.error_code;
        this.waterfallBean.message = waterfallBean.message;
        this.waterfallBean.data.addAll(waterfallBean.data);
        tableAdapter.notifyDataSetChanged();
        water_refresh.setRefreshing(false);
    }


}
