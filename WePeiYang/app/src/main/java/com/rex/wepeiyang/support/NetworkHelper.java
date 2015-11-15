package com.rex.wepeiyang.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.rex.wepeiyang.WePeiYangApp;

/**
 * Created by Rex on 2015/8/4.
 */
public class NetworkHelper {

    public static boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                WePeiYangApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
