package com.twt.service.rxsrc.common;

import android.content.Context;

import com.twt.service.rxsrc.api.ReadApiClient;


/**
 * Created by huangyong on 16/5/18.
 */
public abstract class Presenter {

    protected Context mContext;

    public Presenter(Context context) {
        mContext = context;
    }

    public void onCreate() {

    }

    public void onDestroy() {
        ReadApiClient.getInstance().unSubscribe(mContext);
    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }
}
