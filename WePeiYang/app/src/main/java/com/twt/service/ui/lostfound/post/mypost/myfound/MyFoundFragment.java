package com.twt.service.ui.lostfound.post.mypost.myfound;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mob.tools.gui.PullToRefreshAdatper;
import com.twt.service.R;
import com.twt.service.bean.FoundItem;
import com.twt.service.interactor.FoundInteractorImpl;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.common.OnRcvScrollListener;
import com.twt.service.ui.common.TokenRefreshFailureEvent;
import com.twt.service.ui.common.TokenRefreshSuccessEvent;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.lostfound.found.FoundAdapter;
import com.twt.service.ui.lostfound.post.mypost.myfound.event.GetMyFoundFailureEvent;
import com.twt.service.ui.lostfound.post.mypost.myfound.event.GetMyFoundSuccessEvent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by sunjuntao on 16/4/5.
 */
public class MyFoundFragment extends Fragment implements MyFoundView {
    @InjectView(R.id.rv_found)
    RecyclerView rvFound;
    @InjectView(R.id.srl_found)
    SwipeRefreshLayout srlFound;
    private FoundAdapter adapter;
    private MyFoundPresenter presenter;
    private StaggeredGridLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container, false);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        adapter = new FoundAdapter(getActivity(), FoundAdapter.TYPE_MY_FOUND);
        presenter = new MyFoundPresenterImpl(this, new FoundInteractorImpl());
        presenter.refreshMyFoundItems();
        srlFound.setColorSchemeColors(getResources().getColor(R.color.lost_found_primary_color));
        srlFound.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshMyFoundItems();
            }
        });
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvFound.setLayoutManager(layoutManager);
        rvFound.setAdapter(adapter);
        rvFound.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.loadMoreMyFoundItems();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(TokenRefreshSuccessEvent event) {
        String authorization = event.getRefreshedToken().data;
        PrefUtils.setToken(authorization);
        presenter.afterTokenRefreshed(authorization);
    }

    public void onEvent(TokenRefreshFailureEvent event) {
        presenter.onFailure(event.getError());
    }

    public void onEvent(GetMyFoundSuccessEvent event) {
        presenter.onSuccess(event.getFound());
    }

    public void onEvent(GetMyFoundFailureEvent event) {
        presenter.onFailure(event.getError());
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
    public void startLoginActivity() {
        LoginActivity.actionStart(getActivity(), NextActivity.MyLostFound);
    }
}
