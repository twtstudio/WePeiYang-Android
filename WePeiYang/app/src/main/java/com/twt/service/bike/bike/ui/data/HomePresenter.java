package com.twt.service.bike.bike.ui.data;

import android.content.Context;

import com.twt.service.bike.api.BikeApiClient;
import com.twt.service.bike.api.BikeApiSubscriber;
import com.twt.service.bike.api.OnNextListener;
import com.twt.service.bike.common.Presenter;
import com.twt.service.bike.model.BikeUserInfo;


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
            mViewController.setBikeUserInfo(bikeUserInfo);
        }
    };

    @Override
    public void onDestroy() {
        //取消两个API的订阅
        super.onDestroy();
        BikeApiClient.getInstance().unSubscribe(mContext);
    }
}
