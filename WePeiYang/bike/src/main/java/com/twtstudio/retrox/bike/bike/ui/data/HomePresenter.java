package com.twtstudio.retrox.bike.bike.ui.data;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.twt.wepeiyang.commons.experimental.CommonContext;
import com.twt.wepeiyang.commons.experimental.CommonPreferences;
import com.twtstudio.retrox.bike.api.BikeApiClient;
import com.twtstudio.retrox.bike.api.BikeApiSubscriber;
import com.twtstudio.retrox.bike.api.OnNextListener;
import com.twtstudio.retrox.bike.bike.bikeAuth.BikeAuthActivity;
import com.twtstudio.retrox.bike.common.Presenter;
import com.twtstudio.retrox.bike.model.BikeUserInfo;

import es.dmoral.toasty.Toasty;

/**
 * Created by jcy on 2016/8/9.
 */

public class HomePresenter extends Presenter {
    private HomeViewController mViewController;
    protected OnNextListener<BikeUserInfo> mBikeUserInfoListener = new OnNextListener<BikeUserInfo>() {
        @Override
        public void onNext(BikeUserInfo bikeUserInfo) {
            mViewController.setBikeUserInfo(bikeUserInfo);
            if (bikeUserInfo.status == 1) {
                CommonPreferences.INSTANCE.setBindBike(true);
            } else {
                CommonPreferences.INSTANCE.setBindBike(false);
                Toasty.warning(CommonContext.INSTANCE.getApplication(), "请绑定自行车卡", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, BikeAuthActivity.class);
                mContext.startActivity(intent);
            }
        }
    };

    public HomePresenter(Context context, HomeViewController viewController) {
        super(context);
        mViewController = viewController;
    }

    public void getBikeUserInfo() {
        BikeApiClient.getInstance().getUserInfo(mContext, new BikeApiSubscriber(mContext, mBikeUserInfoListener));
    }

    @Override
    public void onDestroy() {
        //取消两个API的订阅
        super.onDestroy();
        BikeApiClient.getInstance().unSubscribe(mContext);
    }
}
