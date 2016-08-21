package com.twt.service.bike.bike.ui.announcement;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.twt.service.R;
import com.twt.service.bike.common.ui.PFragment;
import com.twt.service.bike.model.BikeAnnouncement;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by jcy on 16-8-20.
 */
public class AnnouncementFragment extends PFragment<AnnouncementPresenter> implements AnnouncementViewController {
    @InjectView(R.id.bike_announcement_rec)
    RecyclerView mRecyclerView;
    @InjectView(R.id.bike_announcement_srl)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private AnnouncementAdapter mAdapter;

    @Override
    protected AnnouncementPresenter getPresenter() {
        return new AnnouncementPresenter(getContext(), this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_bike_announcement;
    }

    @Override
    protected void initView() {
        mAdapter = new AnnouncementAdapter(getContext());
        mSwipeRefreshLayout.setOnRefreshListener(() -> mPresenter.getAnnouncement());
        mSwipeRefreshLayout.post(() -> mPresenter.getAnnouncement());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.showFooter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setAnnouncementList(List<BikeAnnouncement> announcementList) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.refreshItems(announcementList);
    }
}
