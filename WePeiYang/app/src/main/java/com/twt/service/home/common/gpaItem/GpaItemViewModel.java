package com.twt.service.home.common.gpaItem;

import android.app.ActivityOptions;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;


import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.GpaProvider;
import com.twtstudio.retrox.gpa.view.GpaActivity;
import com.twt.service.base.BaseActivity;

import rx.functions.Action1;


/**
 * Created by retrox on 2017/1/21.
 */

public class GpaItemViewModel extends AndroidViewModel implements ViewModel {


    public final MutableLiveData<GpaBean> observableGpa = new MutableLiveData<>();

    public GpaItemViewModel(@NonNull Application application) {
        super(application);
        getData();
    }

    public void getData() {
        GpaProvider.init(getApplication().getApplicationContext())
                .registerAction(observableGpa::setValue)
                .getData(false);
    }

    private void jumpToDetail(View view) {
        Intent intent = new Intent(getApplication().getApplicationContext(), GpaActivity.class);
        //应该在非appcontext中操作

    }

}
