package com.bdpqchen.yellowpagesmodule.yellowpages.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by chen on 17-2-21.
 */
public class ToastUtils {

    private static Toast mToast;

    public static void show(final Activity activity, final String s){
/*
        int duration = Toast.LENGTH_SHORT;
        if(isLong){
            duration = Toast.LENGTH_LONG;
        }
*/

        showAsMainThread(activity, s, Toast.LENGTH_SHORT);
    }

    public static void show(final Activity activity, final String s, boolean isLong){
        if(isLong){
            showAsMainThread(activity, s, Toast.LENGTH_LONG);
        }else{
            show(activity, s);
        }
    }

    public static void showAsMainThread(final Activity context, final String s, final int duration){

        if("main".equals(Thread.currentThread().getName())){
            showToast(context, s, duration);
        }else{
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(context, s, duration);
                }
            });
        }
    }

    private static void showToast(Context context, String s, int time) {
        if(mToast == null){
            mToast = Toast.makeText(context, s, time);
        }else{
            mToast.setText(s);
        }
        mToast.show();
    }

}
