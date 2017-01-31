package com.twt.service.rxsrc.bike.bikeAuth;

import android.content.Context;
import android.util.Log;


import com.twt.service.rxsrc.api.BikeApiClient;
import com.twt.service.rxsrc.api.BikeApiSubscriber;
import com.twt.service.rxsrc.api.OnErrorListener;
import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.common.BikePresenter;
import com.twt.service.rxsrc.model.BikeCard;
import com.twt.service.support.PrefUtils;

import java.util.List;

/**
 * Created by jcy on 2016/8/21.
 */

public class BikeCardPresenter extends BikePresenter {
    private BikeCardViewController mViewController;

    private static final String TAG = "BikeCardPresenter";
    public BikeCardPresenter(Context context,BikeCardViewController viewController) {
        super(context);
        mViewController = viewController;
    }


    public void getBikeCard(String idnum){
        BikeApiClient.getInstance().getBikeCard(mContext,new BikeApiSubscriber(mContext,mListener,errrorListener),idnum);
    }

    private OnNextListener<List<BikeCard>> mListener = bikeCards -> {
        mViewController.setCardList(bikeCards);
    };

    private OnErrorListener errrorListener = new OnErrorListener() {
        @Override
        public void onError(Throwable throwable) {
            mViewController.onError(throwable.toString());
        }
    };

    public void bindBikeCard(){
        BikeApiClient.getInstance().bindBikeCard(mContext,new BikeApiSubscriber(mContext,mBindListener), PrefUtils.getCardId(),PrefUtils.getCardSign());
    }

    protected OnNextListener<String> mBindListener = s -> {
        mViewController.onCardBind();
        Log.d(TAG, "onNext: "+"bindok");
    };
}
