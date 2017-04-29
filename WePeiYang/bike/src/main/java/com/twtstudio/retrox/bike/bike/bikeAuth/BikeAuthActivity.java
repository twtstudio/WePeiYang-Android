package com.twtstudio.retrox.bike.bike.bikeAuth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.bike.ui.main.BikeActivity;
import com.twtstudio.retrox.bike.common.ui.PActivity;
import com.twtstudio.retrox.bike.model.BikeAuth;
import com.twtstudio.retrox.bike.utils.PrefUtils;

import butterknife.BindView;


/**
 * Created by jcy on 2016/8/7.
 */

@Route(path = "/bike/auth")
public class BikeAuthActivity extends PActivity<BikeAuthPresenter> implements BikeAuthController {

    @BindView(R2.id.et_auth_bike)
    EditText mAuthNumEdit;
    @BindView(R2.id.bt_auth_bike)
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
            dismissLoadingDialog();
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
        }else {
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
}
