package com.twt.service.bike.bike.bikeAuth;

import android.content.Context;
import android.util.Log;


import com.twt.service.bike.api.BikeApiClient;
import com.twt.service.bike.api.BikeApiSubscriber;
import com.twt.service.bike.api.OnNextListener;
import com.twt.service.bike.common.BikePresenter;
import com.twt.service.bike.model.BikeCard;
import com.twt.service.support.PrefUtils;

import java.util.List;

/**
 * Created by jcy on 2016/8/21.
 */

public class BikeCardPresenter extends BikePresenter {
    private BikeCardViewController mViewController;

    public BikeCardPresenter(Context context,BikeCardViewController viewController) {
        super(context);
        mViewController = viewController;
    }

    public void getBikeCard(String idnum){
        BikeApiClient.getInstance().getBikeCard(mContext,new BikeApiSubscriber(mContext,mListener),idnum);
    }

    private OnNextListener<List<BikeCard>> mListener = bikeCards -> {
        mViewController.setCardList(bikeCards);
    };

    public void bindBikeCard(){
        BikeApiClient.getInstance().bindBikeCard(mContext,new BikeApiSubscriber(mContext,mBindListener), PrefUtils.getCardId(),PrefUtils.getCardSign());
    }

    protected OnNextListener<String> mBindListener = new OnNextListener<String>() {
        @Override
        public void onNext(String s) {
            //Log.d(TAG, "onNext: "+"bindok");
        }
    };
}
