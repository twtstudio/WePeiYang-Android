package com.twt.service.common;

import android.content.Context;

import com.twt.service.api.WePeiYangClient;

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
        WePeiYangClient.getInstance().unsubscribe(mContext);
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
