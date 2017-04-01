package com.twtstudio.retrox.bike.bike.ui.data;

import android.content.Context;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.bike.api.BikeApiClient;
import com.twtstudio.retrox.bike.api.BikeApiSubscriber;
import com.twtstudio.retrox.bike.api.OnNextListener;
import com.twtstudio.retrox.bike.common.Presenter;
import com.twtstudio.retrox.bike.model.BikeUserInfo;

/**
 * Created by jcy on 2016/8/9.
 */

public class HomePresenter extends Presenter {
    private HomeViewController mViewController;
    public HomePresenter(Context context, HomeViewController viewController) {
        super(context);
        mViewController=viewController;
    }

    public void getBikeUserInfo(){
        BikeApiClient.getInstance().getUserInfo(mContext,new BikeApiSubscriber(mContext,mBikeUserInfoListener));
    }

    protected OnNextListener<BikeUserInfo> mBikeUserInfoListener = new OnNextListener<BikeUserInfo>() {
        @Override
        public void onNext(BikeUserInfo bikeUserInfo) {
            CommonPrefUtil.setIsBindBike(true);
            if (bikeUserInfo.status == 1){
                mViewController.setBikeUserInfo(bikeUserInfo);
            }
        }
    };

    @Override
    public void onDestroy() {
        //取消两个API的订阅
        super.onDestroy();
        BikeApiClient.getInstance().unSubscribe(mContext);
    }
}
