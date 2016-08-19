package com.twt.service.bike.bike.bikeAuth;

import android.content.Context;
import android.util.Log;

import com.twtstudio.wepeiyanglite.api.OnNextListener;
import com.twtstudio.wepeiyanglite.api.bikeApi.BikeApiClient;
import com.twtstudio.wepeiyanglite.api.bikeApi.BikeApiSubscriber;
import com.twtstudio.wepeiyanglite.common.BikePresenter;
import com.twtstudio.wepeiyanglite.model.BikeAuth;
import com.twtstudio.wepeiyanglite.model.BikeCard;
import com.twtstudio.wepeiyanglite.support.PrefUtils;

import java.util.List;

/**
 * Created by jcy on 2016/8/7.
 */

public class BikeAuthPresenter extends BikePresenter {
    private String TAG="BikeAuth";

    private BikeAuthController mViewController;

    public BikeAuthPresenter(Context context, BikeAuthController viewController) {
        super(context);
        mViewController = viewController;
    }

    public void getBikeToken(){
        String wpy_token = PrefUtils.getTokenForBike();
        BikeApiClient.getInstance().getBikeToken(mContext,new BikeApiSubscriber(mContext,mListener),wpy_token);
    }

    private OnNextListener<BikeAuth> mListener = new OnNextListener<BikeAuth>() {
        @Override
        public void onNext(BikeAuth bikeAuth) {
            PrefUtils.setBikeToken(bikeAuth.token);
            Log.d(TAG,bikeAuth.token);
        }
    };

    public void getBikeCard(String idnum){
        BikeApiClient.getInstance().getBikeCard(mContext,new BikeApiSubscriber(mContext,mCardListener),idnum);
    }

    private OnNextListener<List<BikeCard>> mCardListener = new OnNextListener<List<BikeCard>>() {
        @Override
        public void onNext(List<BikeCard> bikeCards) {
            Log.d(TAG,bikeCards.get(0).toString());
            BikeCard card = bikeCards.get(0);
            PrefUtils.setCardId(card.id);
            PrefUtils.setCardSign(card.sign);
        }
    };

    public void bindBikeCard(){
        BikeApiClient.getInstance().bindBikeCard(mContext,new BikeApiSubscriber(mContext,mBindListener),PrefUtils.getCardId(),PrefUtils.getCardSign());
    }

    protected OnNextListener<String> mBindListener = new OnNextListener<String>() {
        @Override
        public void onNext(String s) {
            Log.d(TAG, "onNext: "+"bindok");
        }
    };
}
