package com.twt.service.ui.lostfound.lost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.ui.BaseFragment;
import com.twt.service.ui.common.OnRcvScrollListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Rex on 2015/8/2.
 */
public class LostFragment extends BaseFragment implements LostView, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.rv_lost)
    RecyclerView rvLost;
    @InjectView(R.id.srl_lost)
    SwipeRefreshLayout srlLost;

    LostPresenter presenter;

    private LostAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;

    private int[] lastPositions;//最后一个的位置
    private int lastVisibleItemPosition;//最后一个可见item的position


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lost, container, false);
        ButterKnife.inject(this, rootView);
        srlLost.setColorSchemeColors(getResources().getColor(R.color.lost_found_dark_primary_color));
        srlLost.setOnRefreshListener(this);
        adapter = new LostAdapter(getActivity());
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvLost.setLayoutManager(layoutManager);
        rvLost.setAdapter(adapter);
        rvLost.setOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.lostMoreLostItems();
            }
        });

        return rootView;
    }

    @Override
    public void showRefreshing() {
        srlLost.setRefreshing(true);
    }

    @Override
    public void hideRefreshing() {
        srlLost.setRefreshing(false);
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRefresh() {
        presenter.refreshLostItems();
    }

    private int findMax(int[] dataSet) {
        int max = dataSet[0];
        for (int value : dataSet) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

}
