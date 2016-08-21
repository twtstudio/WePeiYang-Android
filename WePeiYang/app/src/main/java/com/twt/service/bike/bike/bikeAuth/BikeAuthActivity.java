package com.twt.service.bike.bike.bikeAuth;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.twt.service.R;
import com.twt.service.bike.common.ui.PActivity;

import butterknife.InjectView;


/**
 * Created by jcy on 2016/8/7.
 */

public class BikeAuthActivity extends PActivity<BikeAuthPresenter> implements BikeAuthController{

    @InjectView(R.id.et_auth_bike)
    EditText mAuthNumEdit;
    @InjectView(R.id.bt_auth_bike)
    Button mAuthBtn;

    @Override
    protected BikeAuthPresenter getPresenter() {
        return new BikeAuthPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bike_auth;
    }

    @Override
    protected void actionStart(Context context) {

    }

    @Override
    protected int getStatusbarColor() {
        return android.R.color.holo_blue_bright;
    }

    @Override
    protected void initView() {
        //String idnum = mAuthNumEdit.getText().toString();
        mPresenter.getBikeToken();
        showLoadingDialog("正在校验身份...");
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Override
    public void onTokenGot() {

    }
}
