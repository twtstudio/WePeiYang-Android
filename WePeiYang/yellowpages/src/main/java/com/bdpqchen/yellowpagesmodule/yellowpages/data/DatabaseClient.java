package com.bdpqchen.yellowpagesmodule.yellowpages.data;

import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.network.RxSchedulersHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by bdpqchen on 17-3-1.
 */

public class DatabaseClient {

    private static DatabaseClient mClient;

    public static DatabaseClient getInstance(){
        if(mClient == null){
            mClient = new DatabaseClient();
        }
        return mClient;
    }

    public void getCollectedData(Subscriber subscriber) {
        Observable.create(new Observable.OnSubscribe<List<Phone>>() {
            @Override
            public void call(Subscriber<? super List<Phone>> subscriber) {
                List<Phone> list = DataManager.getCollectedDataList();
                subscriber.onNext(list);
//                subscriber.onCompleted();
            }
        }).compose(RxSchedulersHelper.<List<Phone>>io_main())
                .subscribe(subscriber);
    }

    public void getCategoryDataList(Subscriber subscriber) {
        Observable.create(new Observable.OnSubscribe<String[][]>() {
            @Override
            public void call(Subscriber<? super String[][]> subscriber) {
                String[][] list = DataManager.getDepartmentsByCategory();
                subscriber.onNext(list);
//                subscriber.onCompleted();
            }
        }).compose(RxSchedulersHelper.<String[][]> io_main())
                .subscribe(subscriber);
    }


    public void getUnitListByDepartment(Subscriber subscriber, final String toolbarName) {
        Observable.create(new Observable.OnSubscribe<List<Phone>>() {
            @Override
            public void call(Subscriber<? super List<Phone>> subscriber) {
                List<Phone> phoneList = DataManager.getUnitListByDepartment(toolbarName);
                subscriber.onNext(phoneList);

            }
        }).compose(RxSchedulersHelper.<List<Phone>> io_main())
                .subscribe(subscriber);
    }
}
