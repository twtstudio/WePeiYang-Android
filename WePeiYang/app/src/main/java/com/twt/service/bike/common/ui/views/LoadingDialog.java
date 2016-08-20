package com.twt.service.bike.common.ui.views;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by huangyong on 16/5/18.
 */
public class LoadingDialog extends ProgressDialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

}
