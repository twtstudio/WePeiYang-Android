package com.twt.service.rxsrc.bike.bikeAuth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.twt.service.R;
import com.twt.service.rxsrc.bike.ui.main.BikeActivity;
import com.twt.service.rxsrc.common.ui.PActivity;
import com.twt.service.rxsrc.model.BikeCard;
import com.twt.service.support.PrefUtils;

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
        showLoadingDialog("正在验证身份信息...");
        mAdapter = new BikeCardAdapter(this,this);
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
        dismissLoadingDialog();
        mAdapter.refreshItems(cardList);
    }

    @Override
    public void onError(String s) {
        toastMessage(s);
        Intent intent = new Intent(this,BikeAuthActivity.class);
        startActivity(intent);
        //BikeActivity.actionStart(this);
        finish();
    }

    @Override
    public void onCardSelected() {
        mPresenter.bindBikeCard();
        showLoadingDialog("正在绑定");
    }

    @Override
    public void onCardBind() {
        PrefUtils.setBikeIsBindState(true);
        BikeActivity.actionStart(this);
        dismissLoadingDialog();
        finish();
    }

}
