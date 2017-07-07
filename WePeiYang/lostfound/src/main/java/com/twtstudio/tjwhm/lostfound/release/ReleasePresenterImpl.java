package com.twtstudio.tjwhm.lostfound.release;

import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twtstudio.tjwhm.lostfound.base.BaseBean;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public class ReleasePresenterImpl implements ReleaseContract.ReleasePresenter {

    ReleaseContract.ReleaseView releaseView;
    ReleaseApi releaseApi;

    public ReleasePresenterImpl(ReleaseContract.ReleaseView releaseView) {
        this.releaseView = releaseView;
    }

    @Override
    public void updateReleaseData(final Map<String, Object> map, String lostOrFound) {
        releaseApi = RetrofitProvider.getRetrofit().create(ReleaseApi.class);
        releaseApi.updateRelease(map,lostOrFound)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::successCallBack, new RxErrorHandler());
    }

    @Override
    public void successCallBack(BaseBean baseBean) {
        releaseView.successCallBack();
    }
}
