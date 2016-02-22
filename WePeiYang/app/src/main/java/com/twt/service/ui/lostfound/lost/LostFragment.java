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
import com.twt.service.bean.LostItem;
import com.twt.service.interactor.LostInteractorImpl;
import com.twt.service.ui.BaseFragment;
import com.twt.service.ui.common.OnRcvScrollListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Rex on 2015/8/2.
 */
public class LostFragment extends BaseFragment implements LostView, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.rv_lost)
    RecyclerView rvLost;
    @InjectView(R.id.srl_lost)
    SwipeRefreshLayout srlLost;

    LostPresenterImpl presenter;

    private LostAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lost, container, false);
        ButterKnife.inject(this, rootView);
        srlLost.setColorSchemeColors(getResources().getColor(R.color.lost_found_dark_primary_color));
        srlLost.setOnRefreshListener(this);
        presenter = new LostPresenterImpl(this, new LostInteractorImpl());
        adapter = new LostAdapter(getActivity());
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvLost.setLayoutManager(layoutManager);
        rvLost.setAdapter(adapter);
        presenter.refreshLostItems();
        rvLost.setOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.lostMoreLostItems();
            }
        });

        return rootView;
    }

    public void onEvent(SuccessEvent successEvent) {
        presenter.onSuccess(successEvent.getLost());
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
        srlLost.setRefreshing(false);
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshItems(List<LostItem> items) {
        adapter.refreshItems(items);
    }

    @Override
    public void loadMoreItems(List<LostItem> items) {
        adapter.loadMoretems(items);
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
}
