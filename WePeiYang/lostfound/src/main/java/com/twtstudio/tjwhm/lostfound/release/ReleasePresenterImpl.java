package com.twtstudio.tjwhm.lostfound.release;

import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twtstudio.tjwhm.lostfound.base.BaseBean;

import java.util.Map;
import java.util.Objects;

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
        releaseApi.updateRelease(map, lostOrFound)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::successCallBack, new RxErrorHandler());
    }

    @Override
    public void successCallBack(BaseBean baseBean) {
        releaseView.successCallBack();
    }

    @Override
    public void updateEditData(Map<String, Object> map, String lostOrFound, int id) {
        releaseApi = RetrofitProvider.getRetrofit().create(ReleaseApi.class);
        String lof;
        if (Objects.equals(lostOrFound, "editFound")) {
            lof = "found";
        } else {
            lof = "lost";
        }
        releaseApi.updateEdit(map, lof, String.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::successEditCallback, new RxErrorHandler());
    }

    @Override
    public void successEditCallback(BaseBean baseBean) {
        releaseView.successCallBack();
    }

    @Override
    public void delete(int id) {
        releaseApi = RetrofitProvider.getRetrofit().create(ReleaseApi.class);
        releaseApi.delete(String.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::deleteSuccessCallBack, new RxErrorHandler());
    }

    @Override
    public void deleteSuccessCallBack(BaseBean baseBean) {
        releaseView.deleteSuccessCallBack();
    }


}
