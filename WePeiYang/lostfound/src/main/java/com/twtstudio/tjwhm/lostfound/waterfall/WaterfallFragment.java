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
import android.widget.LinearLayout;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.common.internal.Ints.max;

/**
 * Created by tjwhm on 2017/7/3.
 **/

public class WaterfallFragment extends Fragment implements WaterfallContract.WaterfallView {
    @BindView(R2.id.waterfall_refresh)
    SwipeRefreshLayout water_refresh;
    @BindView(R2.id.waterfall_recyclerView)
    RecyclerView waterfall_recyclerView;
    @BindView(R2.id.waterfall_no_res)
    LinearLayout waterfall_no_res;
    private WaterfallTableAdapter tableAdapter;
    private StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
    private WaterfallBean waterfallBean = new WaterfallBean();
    private int page = 1;
    private boolean isLoading = false;
    private boolean isRefresh = false;
    String lostOrFound;
    int type = -1;

    private WaterfallContract.WaterfallPresenter waterfallPresenter = new WaterfallPresenterImpl(this);

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
        View view = inflater.inflate(R.layout.lf_fragment_waterfall, container, false);
        ButterKnife.bind(this, view);
        waterfall_recyclerView.setLayoutManager(layoutManager);
        waterfallBean.data = new ArrayList<>();
        waterfall_no_res.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        lostOrFound = bundle.getString("index");
        tableAdapter = new WaterfallTableAdapter(waterfallBean, getActivity(), lostOrFound);
        waterfall_recyclerView.setAdapter(tableAdapter);
        water_refresh.setOnRefreshListener(this::refresh);


        waterfall_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalCount = layoutManager.getItemCount();
                int[] lastPositions = new int[layoutManager.getSpanCount()];
                layoutManager.findLastCompletelyVisibleItemPositions(lastPositions);

                int lastPosition = max(lastPositions);
                if (!isLoading && totalCount < lastPosition + 2 && lastPosition != -1) {
                    if (type == -1) {
                        ++page;
                        isLoading = true;
                        waterfallPresenter.loadWaterfallData(lostOrFound, page);
                    } else {
                        ++page;
                        isLoading = true;
                        waterfallPresenter.loadWaterfallDataWithType(lostOrFound, page, type);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void setWaterfallData(WaterfallBean waterfallBean) {
        if (waterfallBean.data.size() == 0 && page == 1) {
            waterfall_no_res.setVisibility(View.VISIBLE);
        } else {
            waterfall_no_res.setVisibility(View.GONE);
        }
        this.waterfallBean.error_code = waterfallBean.error_code;
        this.waterfallBean.message = waterfallBean.message;
        if (isRefresh) {
            this.waterfallBean.data.clear();
        }
        this.waterfallBean.data.addAll(waterfallBean.data);
        tableAdapter.notifyDataSetChanged();
        water_refresh.setRefreshing(false);
        isLoading = false;
        isRefresh = false;
    }

    @Override
    public void loadWaterfallDataWithType(int type) {
        this.type = type;
        page = 1;
        isRefresh = true;
        waterfallPresenter.loadWaterfallDataWithType(lostOrFound, page, type);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        isLoading = true;
        isRefresh = true;
        page = 1;
        waterfallPresenter.loadWaterfallData(lostOrFound, page);
        type = -1;
    }
}
