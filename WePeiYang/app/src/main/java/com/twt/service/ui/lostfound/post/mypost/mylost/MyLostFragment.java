package com.twt.service.ui.lostfound.post.mypost.mylost;

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
import com.twt.service.bean.LostItem;
import com.twt.service.interactor.LostInteractorImpl;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.common.OnRcvScrollListener;
import com.twt.service.ui.common.TokenRefreshFailureEvent;
import com.twt.service.ui.common.TokenRefreshSuccessEvent;
import com.twt.service.ui.login.LoginActivity;
import com.twt.service.ui.lostfound.lost.LostAdapter;
import com.twt.service.ui.lostfound.post.mypost.mylost.event.GetMyLostFailureEvent;
import com.twt.service.ui.lostfound.post.mypost.mylost.event.GetMyLostSuccessEvent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by sunjuntao on 16/4/5.
 */
public class MyLostFragment extends Fragment implements MyLostView {
    @InjectView(R.id.rv_lost)
    RecyclerView rvLost;
    @InjectView(R.id.srl_lost)
    SwipeRefreshLayout srlLost;
    private MyLostPresenter presenter;
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
        View view = inflater.inflate(R.layout.fragment_lost, container, false);
        ButterKnife.inject(this, view);
        presenter = new MyLostPresenterImpl(this, new LostInteractorImpl());
        adapter = new LostAdapter(getActivity());
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvLost.setLayoutManager(layoutManager);
        rvLost.setAdapter(adapter);
        rvLost.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.loadMoreMyLostItems();
            }
        });
        presenter.refreshMyLostItems();
        srlLost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshMyLostItems();
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

    public void onEvent(GetMyLostSuccessEvent event) {
        presenter.onSuccess(event.getLost());
    }

    public void onEvent(GetMyLostFailureEvent event) {
        presenter.onFailure(event.getError());
    }

    public void onEvent(TokenRefreshSuccessEvent event) {
        String authorization = event.getRefreshedToken().data;
        PrefUtils.setToken(authorization);
        presenter.afterTokenRefreshed(authorization);
    }

    public void onEvent(TokenRefreshFailureEvent event) {
        presenter.onFailure(event.getError());
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
    public void startLoginActivity() {
        LoginActivity.actionStart(getActivity(), NextActivity.MyLostFound);
    }
}
