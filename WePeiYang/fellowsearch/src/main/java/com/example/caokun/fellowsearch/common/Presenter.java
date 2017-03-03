package com.example.caokun.fellowsearch.common;

import android.content.Context;

import com.example.caokun.fellowsearch.model.FellowApi;
import com.example.caokun.fellowsearch.model.FellowApiClient;


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
        FellowApiClient.getInstance().unSubscribe(mContext);
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
