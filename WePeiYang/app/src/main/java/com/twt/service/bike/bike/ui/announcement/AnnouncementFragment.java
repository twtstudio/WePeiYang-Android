package com.twt.service.bike.bike.ui.announcement;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.twt.service.R;
import com.twt.service.bike.common.ui.PFragment;

import butterknife.InjectView;

/**
 * Created by jcy on 16-8-20.
 */
public class AnnouncementFragment extends PFragment<AnnouncementPresenter> implements AnnouncementViewController {
    @InjectView(R.id.bike_announcement_rec)
    RecyclerView mRecyclerView;
    @InjectView(R.id.bike_announcement_srl)
    SwipeRefreshLayout mSwipeRefreshLayout;

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

    }
}
