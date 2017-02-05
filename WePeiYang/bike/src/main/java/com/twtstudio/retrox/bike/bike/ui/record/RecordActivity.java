package com.twtstudio.retrox.bike.bike.ui.record;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.common.ui.PActivity;
import com.twtstudio.retrox.bike.model.BikeRecord;

import java.util.List;

import butterknife.BindView;


/**
 * Created by jcy on 2016/9/9.
 */
public class RecordActivity extends PActivity<RecordPresenter> implements RecordViewController{

    @BindView(R2.id.record_rcv)
    RecyclerView mRecyclerView;
    @BindView(R2.id.bike_record_toolbar)
    Toolbar mToolbar;
    RecordAdapter mAdapter;

    @Override
    protected RecordPresenter getPresenter() {
        return new RecordPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bike_record;
    }

    @Override
    protected void actionStart(Context context) {

    }

    @Override
    protected int getStatusbarColor() {
        return R.color.bike_toolbar_color;
    }

    @Override
    protected void initView() {
        mAdapter = new RecordAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getCurrentRecordList();
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void refreshItems(List<BikeRecord> recordList) {
        mAdapter.refreshItems(recordList);
        mAdapter.hideFooter();
    }
}
