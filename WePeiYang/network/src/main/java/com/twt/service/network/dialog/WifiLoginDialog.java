package com.twt.service.network.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * Created by chen on 2017/8/31.
 */

public class WifiLoginDialog extends Dialog {
    public WifiLoginDialog(@NonNull Context context) {
        super(context);
    }

    public WifiLoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected WifiLoginDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
