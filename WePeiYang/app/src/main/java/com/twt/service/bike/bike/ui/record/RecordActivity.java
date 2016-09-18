package com.twt.service.bike.bike.ui.record;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.twt.service.R;
import com.twt.service.bike.common.ui.PActivity;

import butterknife.InjectView;

/**
 * Created by jcy on 2016/9/9.
 */
public class RecordActivity extends PActivity<RecordPresenter> implements RecordViewController{

    @InjectView(R.id.record_rcv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.record_rcv)
    Toolbar mToolbar;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }
}
