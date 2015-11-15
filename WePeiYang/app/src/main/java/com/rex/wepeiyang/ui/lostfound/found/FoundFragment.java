package com.rex.wepeiyang.ui.lostfound.found;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.ui.common.OnRcvScrollListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Rex on 2015/8/2.
 */
public class FoundFragment extends Fragment implements FoundView, SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.rv_found)
    RecyclerView rvFound;
    @InjectView(R.id.srl_found)
    SwipeRefreshLayout srlFound;
    private StaggeredGridLayoutManager layoutManager;
    private FoundAdapter adapter;
    private  FoundPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container, false);
        ButterKnife.inject(this, view);
        srlFound.setColorSchemeColors(getResources().getColor(R.color.lost_found_dark_primary_color));
        srlFound.setOnRefreshListener(this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new FoundAdapter(getActivity());
        rvFound.setLayoutManager(layoutManager);
        rvFound.setAdapter(adapter);
        rvFound.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.loadMoreFoundItems();
            }
        });
        return view;
    }

    @Override
    public void showRefreshing() {
        srlFound.setRefreshing(true);
    }

    @Override
    public void hideRefreshing() {
        srlFound.setRefreshing(false);
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
        presenter.refreshFoundItems();
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }

        }
        return max;
    }

}
