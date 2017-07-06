package com.twtstudio.retrox.tjulibrary.provider;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.network.ApiResponse;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/21.
 */

public class TjuLibProvider {

    private LibApi libApi;

    private Context mContext;

    public TjuLibProvider(Context context) {
        libApi = RetrofitProvider.getRetrofit().create(LibApi.class);
        mContext = context;
    }

    /**
     * bind tju lib
     *
     * @param action1   -1:ok others:to Api doc
     * @param libpasswd
     */
    public void bindLibrary(Action1<Integer> action1, String libpasswd) {

        libApi.bindLib(libpasswd).map(ApiResponse::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> action1.call(-1), new RxErrorHandler(mContext));

    }

    public void unbindLibrary(Action1<String> action1) {
        libApi.unbindLib().subscribeOn(Schedulers.io())
                .map(ApiResponse::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    action1.call(s);
                    Logger.d("解绑图书馆：" + s);
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                }, new RxErrorHandler());
    }

    public void getUserInfo(Action1<Info> action1) {

        libApi.getLibUserInfo().map(ApiResponse::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new RxErrorHandler(mContext));

    }

    public void getUserInfo(Action1<Info> action1, Action1<Throwable> throwableAction1) {

        Observable<Info> infoObservable =
                libApi.getLibUserInfo().map(ApiResponse::getData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        /**
         * 因为okhttp的异常信息传递过程中，错误体是通过流的方式传输，被读取一次后就特么没了
         */
        infoObservable.subscribe(action1, throwableAction1);
        infoObservable.subscribe(info -> {}, new RxErrorHandler(mContext));

    }

    public void getUserHistory(Action1<List<Histroy>> action1) {
        libApi.getLibUserHistroy().map(ApiResponse::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new RxErrorHandler(mContext));
    }

    public void renewAllBooks(Action1<List<RenewResult>> action1) {
        renewBooks(action1, "all");
    }

    public void renewBooks(Action1<List<RenewResult>> action1, String barcode) {

        libApi.renewBooks(barcode).map(ApiResponse::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new RxErrorHandler(mContext));

    }


}
