package com.twtstudio.retrox.bike.bike.bikeAuth;

import android.content.Context;
import android.util.Log;

import com.twtstudio.retrox.bike.api.BikeApiClient;
import com.twtstudio.retrox.bike.api.BikeApiSubscriber;
import com.twtstudio.retrox.bike.api.OnErrorListener;
import com.twtstudio.retrox.bike.api.OnNextListener;
import com.twtstudio.retrox.bike.common.BikePresenter;
import com.twtstudio.retrox.bike.model.BikeCard;
import com.twtstudio.retrox.bike.utils.PrefUtils;

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
