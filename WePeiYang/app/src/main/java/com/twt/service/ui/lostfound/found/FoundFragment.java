package com.twt.service.ui.lostfound.found;

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

import com.twt.service.R;
import com.twt.service.bean.FoundItem;
import com.twt.service.interactor.FoundInteractorImpl;
import com.twt.service.ui.common.OnRcvScrollListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

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
    private FoundPresenterImpl presenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container, false);
        ButterKnife.inject(this, view);
        srlFound.setColorSchemeColors(getResources().getColor(R.color.lost_found_dark_primary_color));
        srlFound.setOnRefreshListener(this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new FoundAdapter(getActivity());
        presenter = new FoundPresenterImpl(this, new FoundInteractorImpl());
        rvFound.setLayoutManager(layoutManager);
        rvFound.setAdapter(adapter);
        rvFound.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.loadMoreFoundItems();
            }
        });
        presenter.refreshFoundItems();
        return view;
    }


    public void onEvent(SuccessEvent successEvent) {
        presenter.onSuccess(successEvent.getFound());
    }

    public void onEvent(FailureEvent failureEvent) {
        presenter.onFailure(failureEvent.getError());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
    public void refreshItems(List<FoundItem> items) {
        adapter.refreshItems(items);
    }

    @Override
    public void loadMoreItems(List<FoundItem> items) {
        adapter.loadMoreItems(items);
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

}
