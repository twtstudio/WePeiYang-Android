package com.example.caokun.fellowsearch;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by huangyong on 16/5/18.
 */
public class ToastUtils {

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
