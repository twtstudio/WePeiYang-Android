package com.twt.service.bike.bike.bikeAuth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.twt.service.R;
import com.twt.service.bike.common.ui.PActivity;
import com.twt.service.bike.model.BikeCard;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by jcy on 2016/8/21.
 */

public class BikeCardActivity extends PActivity<BikeCardPresenter> implements BikeCardViewController{
    @InjectView(R.id.bike_card_toolbar)
    Toolbar mToolBar;
    @InjectView(R.id.bike_card_rcv)
    RecyclerView mRcv;
    private BikeCardAdapter mAdapter;
    private String mIdNumber;

    @Override
    protected BikeCardPresenter getPresenter() {
        return new BikeCardPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bike_card;
    }

    @Override
    protected void actionStart(Context context) {
        Intent intent = getIntent();
        mIdNumber=intent.getStringExtra("idnum");
    }

    @Override
    protected int getStatusbarColor() {
        return R.color.lost_found_indicator_color;
    }

    @Override
    protected void initView() {
        mAdapter = new BikeCardAdapter(this);
        mRcv.setLayoutManager(new LinearLayoutManager(this));
        mRcv.setAdapter(mAdapter);
        mPresenter.getBikeCard(mIdNumber);
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolBar;
    }

    @Override
    public void setCardList(List<BikeCard> cardList) {
        mAdapter.refreshItems(cardList);
    }
}
