package com.twt.service.bike.bike.bikeAuth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.twt.service.R;
import com.twt.service.bike.bike.ui.main.BikeActivity;
import com.twt.service.bike.common.ui.PActivity;
import com.twt.service.bike.model.BikeAuth;
import com.twt.service.support.PrefUtils;

import butterknife.InjectView;


/**
 * Created by jcy on 2016/8/7.
 */

public class BikeAuthActivity extends PActivity<BikeAuthPresenter> implements BikeAuthController {

    @InjectView(R.id.et_auth_bike)
    EditText mAuthNumEdit;
    @InjectView(R.id.bt_auth_bike)
    Button mAuthBtn;

    private String mIdNum;

    @Override
    protected BikeAuthPresenter getPresenter() {
        return new BikeAuthPresenter(this, this);
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
        return R.color.bike_background;
    }

    @Override
    protected void initView() {
        //String idnum = mAuthNumEdit.getText().toString();
        mPresenter.getBikeToken();
        showLoadingDialog("正在校验身份...");
        mAuthBtn.setOnClickListener(view -> {
            mIdNum = mAuthNumEdit.getText().toString();
            Intent intent = new Intent(BikeAuthActivity.this,BikeCardActivity.class);
            intent.putExtra("idnum",mIdNum);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }


    @Override
    public void onTokenGot(BikeAuth bikeAuth) {
        if (bikeAuth.status == 1) {
            PrefUtils.setBikeIsBindState(true);
            BikeActivity.actionStart(this);
            finish();
        }
        dismissLoadingDialog();
        new MaterialDialog.Builder(this)
                .title("注意")
                .content("在您进行新办卡、修改卡信息、换卡等操作后，第二天才能正常使用本系统")
                .positiveText("OK")
                .onPositive((dialog, which) -> finish())
                .negativeText("继续绑定")
                .show();
    }
}
