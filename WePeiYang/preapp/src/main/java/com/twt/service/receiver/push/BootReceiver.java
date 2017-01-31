package com.twt.service.receiver.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.twt.service.service.push.PushService;

/**
 * Created by jcy on 2016/7/7.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            PushService.actionStart(context);
        }
    }
}
