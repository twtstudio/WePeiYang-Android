package com.twtstudio.retrox.bike.service;

import android.content.Context;
import android.widget.Toast;

import com.twtstudio.retrox.bike.api.BikeApiClient;
import com.twtstudio.retrox.bike.api.BikeApiResponse;
import com.twtstudio.retrox.bike.api.BikeApiSubscriber;
import com.twtstudio.retrox.bike.api.OnNextListener;

import es.dmoral.toasty.Toasty;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 29/04/2017.
 */

public class BikeServiceProvider {

    private Context mContext;

    public BikeServiceProvider(Context mContext) {
        this.mContext = mContext;
    }

    public void unbind() {
        BikeApiClient.getInstance().getService().unbindBikeCard("fake")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BikeApiSubscriber<BikeApiResponse<Void>>(mContext, new OnNextListener<BikeApiResponse<Void>>() {
                    @Override
                    public void onNext(BikeApiResponse<Void> voidBikeApiResponse) {
                        Toasty.success(mContext, "自行车解绑成功！请重启微北洋", Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
