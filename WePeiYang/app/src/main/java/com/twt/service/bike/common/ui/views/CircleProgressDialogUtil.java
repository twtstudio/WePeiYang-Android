package com.twt.service.bike.common.ui.views;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by jcy on 2016/8/1.
 */

public class CircleProgressDialogUtil {
    private MaterialDialog mDialog;

    public void showDialog(Context context, String title, String content) {
        mDialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(true, 0)
                .show();

    }

    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private static class SingletonHolder {
        private static final CircleProgressDialogUtil INSTANCE = new CircleProgressDialogUtil();
    }

    public static CircleProgressDialogUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
