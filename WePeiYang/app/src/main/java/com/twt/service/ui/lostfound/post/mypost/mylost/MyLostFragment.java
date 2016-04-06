package com.twt.service.ui.lostfound.post.mypost.mylost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.R;
import com.twt.service.interactor.LostInteractorImpl;
import com.twt.service.ui.lostfound.post.mypost.mylost.event.GetMyLostFailureEvent;
import com.twt.service.ui.lostfound.post.mypost.mylost.event.GetMyLostSuccessEvent;

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
}
